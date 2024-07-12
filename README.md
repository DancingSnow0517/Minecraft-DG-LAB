# Minecraft-DG-LAB

English | [简体中文](README_cn.md)

A Mod to connect minecraft with DgLab

### Usage

1. Download this mod and kubejs, put them to mods folder, start the game.
2. write kubejs scripts to control how the strength change

    this is example script

    ```javascript
    // More event see https://wiki.latvian.dev/books/kubejs/page/list-of-events
    EntityEvents.death('player', event => {
        // use player uuid to find exist connection
        const connection = DgLabManager.getByUUID(event.getEntity().getUuid())
    
        // If this is not null, it means that the player's dglab has already been connected to the server
        if (connection != null) {
            // get the player current strength
            connection.getStrength().getACurrentStrength()
            connection.getStrength().getBCurrentStrength()
            connection.getStrength().getAMaxStrength()
            connection.getStrength().getBMaxStrength()
        
            // Add 10 strength to channel A
            connection.addStrength(ChannelType.A, 10)
        }
    })
   
    EntityEvents.afterHurt('player', event => {
        // use player uuid to find exist connection
        let connection = DgLabManager.getByUUID(event.getEntity().getUuid())
    
        // If this is not null, it means that the player's dglab has already been connected to the server
        if (connection != null) {
            let damage = event.getDamage()
            let strength = 0;
            if (damage >= 20) {
                strength = 100
            } else {
                strength = Math.ceil((damage / 20) * 100)
            }
            // use DgLabPulseUtil.pulse(int...) to convent pulse data
            // the method parameter is frequency and strength alternation, Each segment contains 25ms of data
            // The frequency ranges from 10-1000 and the strength ranges from 0-100
            // ex. DgLabPulseUtil.pulse(500, 10, 500, 50) 
            // These are two small segments, 500 frequency, 10 strength and 500 frequency, 50 strength
            let pulse = DgLabPulseUtil.pulse(
                500, 0,
                500, 0,
                500, 0,
                500, 0,
                500, Math.ceil(strength / 3),
                500, Math.ceil(strength / 3),
                500, Math.ceil(strength / 3),
                500, Math.ceil(strength / 3),
                500, Math.ceil((strength / 3) * 2),
                500, Math.ceil((strength / 3) * 2),
                500, Math.ceil((strength / 3) * 2),
                500, Math.ceil((strength / 3) * 2),
                500, Math.ceil(strength),
                500, Math.ceil(strength),
                500, Math.ceil(strength),
                500, Math.ceil(strength),
                500, Math.ceil((strength / 3) * 2),
                500, Math.ceil((strength / 3) * 2),
                500, Math.ceil((strength / 3) * 2),
                500, Math.ceil((strength / 3) * 2),
                500, Math.ceil(strength / 3),
                500, Math.ceil(strength / 3),
                500, Math.ceil(strength / 3),
                500, Math.ceil(strength / 3),
                500, 0,
                500, 0,
                500, 0,
                500, 0
            )
   
            // addPulse methon to add your pulse to channel A
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
