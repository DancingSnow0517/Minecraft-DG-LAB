# Minecraft-DG-LAB

[English](README.md) | 简体中文

一个 mod 将 Minecraft 与 DgLab（郊狼）连接在一起

### 使用方法

1. 下载这个 mod 和 kubejs, 将他们放入 `mods` 文件夹然后启动游戏。
2. 编写一个 kubejs 脚本来控制强度如何去变化

    这是一个示例脚本

    ```javascript
    // 更多事件可以查看 https://wiki.latvian.dev/books/kubejs/page/list-of-events
    EntityEvents.death('player', event => {
    // 使用玩家的 UUID 来查找连接的 connection
    let connection = DgLabManager.getByUUID(event.getEntity().getUuid())

        // 如果这不是 null, 说明该玩家的 DgLab（郊狼）已经和服务器连接
        if (connection != null) {
            // 获取这个玩家的当前强度，可以发挥想象来使用这些值
            connection.getStrength().getACurrentStrength()
            connection.getStrength().getBCurrentStrength()
            connection.getStrength().getAMaxStrength()
            connection.getStrength().getBMaxStrength()
    
            // 添加 10 点强度到通道 A
            connection.addStrength('a', 10)
        }
    })


    EntityEvents.afterHurt('player', event => {
    // 使用玩家的 UUID 来查找连接的 connection
    let connection = DgLabManager.getByUUID(event.getEntity().getUuid())

        // 如果这不是 null, 说明该玩家的 DgLab（郊狼）已经和服务器连接
        if (connection != null) {
            let damage = event.getDamage()
            let strength = 0;
            if (damage >= 20) {
                strength = 100
            } else {
                strength = Math.ceil((damage / 20) * 100)
            }

            // 波形频率是 10-1000 之间的值，强度是 0-100 之间的值
        
            // 使用方法 sinPulse(频率, 强度最小值, 强度最大值, 持续时间) 来生成一段正弦函数波形
            // 持续时间是 25ms 为单位, 例：40 就是持续 1 秒
            let pulse = DgLabPulseUtil.sinPulse(400, 20, strength, 40)

            // 使用方法 gradientPulse(频率, 开始强度, 结束强度, 持续时间) 来生成一段渐变波形
            // let pulse = DgLabPulseUtil.gradientPulse(400, 20, strength, 40)

            // 使用方法 smoothPulse(频率, 强度, 持续时间) 来生成一段没有强度变化的波形
            // let pulse = DgLabPulseUtil.smoothPulse(400, strength, 40)

            // 也可以使用波形转换原始方法 pulse(int...) 来自定义你的波形
            // 这个方法的参数是 频率 和 强度 交替，每一小段是 25ms 的数据
            // 例： DgLabPulseUtil.pulse(500, 10, 500, 50)
            // 这就是两小段，是500频率，10强度和500频率，50强度
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
   
            // 使用方法 clearPulse(通道) 方法来清除当前通道的所有波形队列
            connection.clearPulse('a')

            // 使用方法 addPulse(通道, 波形数据) 方法来添加你的波形到某个通道
            connection.addPulse('a', pulse)
        }
    })
    ```

    这样示例脚本会运行在玩家死亡后，这意味着每当玩家死亡，他的 DgLab（郊狼）会添加 10 点强度到通道 A。
    
    以及会在玩家受到伤害后，根据收到的伤害来计算最大强度，生成一个渐渐高再低的波形发生到通道A。

3. 在游戏用运行命令 `/dglab connect`, 你将会得到一个连接，包含了一个二维码，在 DG-LAB APP 中扫描这个二维码后，将会连接你的 APP 到游戏服务器中。

### 配置

你可以在 `.minecraft/config/dglab.yml` 查看 mod 的配置文件，这个文件会在你第一次启动后生成。

- address (String)
  * 一个字符串来设置 WebSocket 连接地址，如果你的电脑和你的安装 DG-LAB 的手机在同一个局域网，可以设置此值为类似 `192.168.xx.xx`，这是你电脑的 ip 地址，可以运行 `ipconfig` 来查看。
- port (int)
  * 运行 WebSocket Server 的端口，将会在这个端口的 WebSocket Server 和你的 DG-LAB APP 连接。
