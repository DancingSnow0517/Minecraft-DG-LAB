# Minecraft-DG-LAB

English | [简体中文](README_cn.md)

A Mod to connect minecraft with DgLab

### Usage

1. Download this mod and kubejs, put them to mods folder, start the game.
2. write kubejs scripts to control how the strength change

    this is example script

    ```javascript
    // More event see https://wiki.latvian.dev/books/kubejs/page/list-of-events
    // Death event for 'player'
    EntityEvents.death('player', event => {
        // Use the player's UUID to find the connected connection
        let connection = DgLabManager.getByUUID(event.getEntity().getUuid())

        // If it's not null, it means the player's DgLab (郊狼) is connected to the server
        if (connection != null) {
            // Get the player's current strengths; you can use these values creatively
            connection.getStrength().getACurrentStrength()
            connection.getStrength().getBCurrentStrength()
            connection.getStrength().getAMaxStrength()
            connection.getStrength().getBMaxStrength()
    
            // Add 10 strength points to channel A
            connection.addStrength('a', 10)
        }
    })

    // AfterHurt event for 'player'
    EntityEvents.afterHurt('player', event => {
        // Use the player's UUID to find the connected connection
        let connection = DgLabManager.getByUUID(event.getEntity().getUuid())

        // If it's not null, it means the player's DgLab (郊狼) is connected to the server
        if (connection != null) {
            let damage = event.getDamage()
            let strength = 0;
            if (damage >= 20) {
                strength = 100
            } else {
                strength = Math.ceil((damage / 20) * 100)
            }

            // The waveform frequency is a value between 10 and 1000, and strength is between 0 and 100
        
            // Use sinPulse(frequency, minimum strength, maximum strength, duration) to generate a sinusoidal wave
            // Duration is in units of 25ms; for example, 40 is 1 second duration
            let pulse = DgLabPulseUtil.sinPulse(400, 20, strength, 40)

            // Use gradientPulse(frequency, starting strength, ending strength, duration) to generate a gradient wave
            // let pulse = DgLabPulseUtil.gradientPulse(400, 20, strength, 40)

            // Use smoothPulse(frequency, strength, duration) to generate a pulse without strength variation
            // let pulse = DgLabPulseUtil.smoothPulse(400, strength, 40)

            // You can also use the raw waveform transformation method pulse(int...) to customize your waveform
            // The parameters alternate between frequency and strength, with each segment being data for 25ms
            // Example: DgLabPulseUtil.pulse(500, 10, 500, 50)
            // This represents two segments: 500 frequency, 10 strength and 500 frequency, 50 strength
            // let pulse = DgLabPulseUtil.pulse(
            //     500, 0,
            //     500, 0,
            //     500, 0,
            //     500, 0,
            //     500, Math.ceil(strength / 3),
            //     500, Math.ceil(strength / 3),
            //     500, Math.ceil(strength / 3),
            //     500, Math.ceil(strength / 3),
            //     500, Math.ceil((strength / 3) * 2),
            //     500, Math.ceil((strength / 3) * 2),
            //     500, Math.ceil((strength / 3) * 2),
            //     500, Math.ceil((strength / 3) * 2),
            //     500, Math.ceil(strength),
            //     500, Math.ceil(strength),
            //     500, Math.ceil(strength),
            //     500, Math.ceil(strength),
            //     500, Math.ceil((strength / 3) * 2),
            //     500, Math.ceil((strength / 3) * 2),
            //     500, Math.ceil((strength / 3) * 2),
            //     500, Math.ceil((strength / 3) * 2),
            //     500, Math.ceil(strength / 3),
            //     500, Math.ceil(strength / 3),
            //     500, Math.ceil(strength / 3),
            //     500, Math.ceil(strength / 3),
            //     500, 0,
            //     500, 0,
            //     500, 0,
            //     500, 0
            // )
   
            // method "clearPulse(channel)" clears all waveform queues for the current channel.
            connection.clearPulse('a')

            // Use addPulse(channel, waveform data) to add your waveform to a channel
            connection.addPulse('a', pulse)
        }
    })

    ```

    the example script run when player death, that means when player dead, will add 10 strength to channel A.

   And after the player receives damage, the maximum strength will be calculated based on the damage received, generating a gradually increasing and decreasing waveform that occurs in channel A.

3. in game use command `/dglab connect`, you will get a link with QR code, and scan it in app, will connect to the game.

### Configuration

you can see the config file in `.minecraft/config/dglab.yml`, when you first start game will generate this file.

- address (String)
  * A String to set the ws address, if your computer and your phone installed `DG-LAB` in same LAN, can set it to `192.168.xx.xx` that your computer ip address
- port (int)
  * the port to create ws server, and DG-LAB app will connect it.
