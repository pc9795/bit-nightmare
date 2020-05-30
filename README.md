# BIT NIGHTMARE

* Thanks [Craftpix](https://craftpix.net/) and [Open Game Art](https://opengameart.org/) for amazing assets.
* Thanks [1001 Fonts](https://www.1001fonts.com/) for excellent fonts.
* Thanks [DL Sounds](https://www.dl-sounds.com/) for stunning sounds.
* Thanks [RealTutsGML](https://www.youtube.com/user/RealTutsGML) for phenomenal tutorial on Java game programming.

**Gameplay Video** - https://www.youtube.com/watch?v=0y7qBGYuIDE

## Genre/Theme
A 2-D platformer where a programmer is trapped in an Alien prison

![Long Jumps](readme_pngs/long_jumps.png)

Long jumps on narrow platforms.

![Moving Blocks and Hiding Platforms](readme_pngs/moving_blocks_and_hiding_platforms.png)

Moving blocks and Hiding platforms.

![Nerve-breaking Running Sequences](readme_pngs/running_sequences.png)

Nerve-breaking running sequences.

![Mayhem battles](readme_pngs/mayhem_battles.png)

Mayhem battles.

![Boss Battle](readme_pngs/boss_battle.png)

Boss battle.

**Extras**

* An Arcade style sound track.
* Artwork to give a sci-fi look and feel.

## Features

**Level Designer**

* Currently the game has two levels.
* A basic level designer is implemented that can generate complete levels from .png files
* Everything is Dynamic. We can add levels without changing a single line of code(Until we want to add new game objects).

**Difficulty Options**

![Difficulty options](readme_pngs/difficulty_options.png)

* 3 difficulty options
* Affect different properties of enemies such as their health, line of sight, bullet frequency, etc.

**Weapons**

![Bit Revolver](readme_pngs/bit_revolver.png)

* Bit Revolver
* Shoots single projectiles

![Bit Array Gun](readme_pngs/bit_array_gun.png)

* Bit Array Gun
* Shoots stream of projectiles

![Bit Matrix Blast](readme_pngs/bit_matrix_blast.png)

* Bit Matrix Blast
* Shoots single projectile but with bigger area and high damage.

**Checkpoints**

![Checkpoints](readme_pngs/checkpoints.png)

**Collision Detection**

* Rectangle based collision detection.
* Abstracted some collision scenarios as behaviours and created `BulletCollider`, `EnemyCollider`, `FineGrainedCollider`.
* Quad tree implementation for static environment objects and collectibles. Implemented chopping.

**Texture Loader**

![Config Sample 1](readme_pngs/texture_loader_config1.png)

![Config Sample 2](readme_pngs/texture_loader_config2.png)

* 100% configurable
* Can be used with both single images and sprite sheets.

**Story Loader**

![Config](readme_pngs/story_loader_config.png)

![Example](readme_pngs/story_loader_example.png)

Stories can be created by giving the level name and the x-coordinate of the player.

**Enemies with Basic AI**

![Charger](readme_pngs/charger.png)

* Charger
* Runs towards you if under LOS.

![Soldier](readme_pngs/soldier.png)

* Soldier
* Fires Bit revolver towards you if under LOS.
* Will duck occasionally

![Super Soldier](readme_pngs/super_soldier.png)

* Super Soldier
* Fires Bit Array gun towards you if under LOS.
* Will duck occasionally.

![Guardian](readme_pngs/guardian.png)

* Guardian
* Fires Dual Bit Array guns and Bit Matrix blast towards you if under LOS.
* Will charge towards you occasionally.

**Intelligent/Interactive Environments**

* Movable blocks
* Hiding platforms
* Enemy portals – Spawn enemies if you are under LOS
* Gate and Key pairs

## Functional Game

**Lives**

![Lives](readme_pngs/lives.png)

![Game Over](readme_pngs/game_over.png)

**Ending**

![Pre-Ending](readme_pngs/ending1.png)

![Ending Screen](readme_pngs/ending2.png)

The game can be won by defeating the guardian at the second level and acquiring his key to open the last gate of the game.

## Multiple Controllers

**Mouse interactions**

![Menu](readme_pngs/menu.png)

![Mouse Interaction](readme_pngs/mouse_interaction.png)

* On Menu
* By right clicking on objects

**Gamepad/Keyboard**

![Controller Layout](readme_pngs/controller_layout.png)

