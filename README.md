# AntiSeedCracker

A Spigot plugin making an effort to work against seed cracking mods.

## Requirements
- ProtocolLib 5.3+
- Spigot (and forks) 1.20.4 - 1.21 - Make sure to update to the latest commit/version

*The plugin will - to a degree - check for old versions of ProtocolLib and warn you once on the first player join.*

## Features
- Randomization of hashed seed on login/respawn/world change
- Modification of end spikes
- Modification of end cities

## Help wanted/Features planned

- [Biome name randomization](https://wiki.vg/Registry_Data#Biome) (RealisticSeasons does this, but it's a lot of effort)
- Modification of more structures, there is a brand-new an API we can make use of (Chunk#getStructures).

## Build

```gradle
gradle build
```
