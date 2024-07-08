# Minecraft-DG-LAB

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
    ```

    the example script run when player death, that means when player dead, will add 10 strength to channel A.

3. in game use command `/dglab connect`, you will get a link with QR code, and scan it in app, will connect to the game.

### Configuration

you can see the config file in `.minecraft/config/dglab.yml`, when you first start game will generate this file.

- address (String)
  * A String to set the ws address, if your computer and your phone installed `DG-LAB` in same LAN, can set it to `192.168.xx.xx` that your computer ip address
- port (int)
  * the port to create ws server, and DG-LAB app will connect it.
