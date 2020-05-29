# BossbarAPI
A library for creating bossbars.
## Requirements
ProtocolLib and Spigot 1.8.8 are required for this library.
## Example
```java
Bossbar bossbar = BossBarAPI.getBossBar(player);
bossbar.setMessage(ChatColor.GREEN + "Example bossbar");
bossbar.setPercentage(50);
bossbar.setVisible(true);
```