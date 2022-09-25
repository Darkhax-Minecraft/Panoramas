# Panoramas

The Panoramas mod provides a system for mods and resource packs to add new main manu panoramas to the game.

## Included Panoramas

The mod comes with a few built in panoramas that will be available by default.

- Vanilla (minecraft:vanilla) - The default panorama as defined by Mojang.
- Frozen Ocean (panoramas:frozen_ocean) - A view of the ocean surrounded by icebergs and polar bears.
- Forest (panoramas:forest) - A view of the traditional Minecraft forest.
- Kelp Sea (panoramas:kelp_sea) - An underwater view of a kelp forest with ship wrecks in the background.
- Title Seed (panoramas:title_seed) - A recreation of the original Minecraft title screen panorama.

## Configuration Options

The config file will be read from your instance's config folder. The file will be named `panoramas-client.toml`. If the
file does not exist, you must install and run the mod at least once to generate the most up to date file.

- `removedEntries` - This option allows you to filter out panorama entries using their full ID. If the ID is listed here
  it will never be displayed. For example `removedEntries = ["minecraft:vanilla]` will prevent the vanilla panorama from
  being displayed.
- `removedNamespaces` - This option allows all panoramas from a given namespace to be filtered. The namespace is the
  first half of an ID before the `:`. For example `removedNamespaces = ["panoramas"]` will remove all panoramas included
  with the mod by default.
- `maxDisplayTime` - This controls the time in milliseconds to display the panorama. The mod will try to cycle to a new
  panorama after this duration of time. The default is 600 seconds.
- `forcedPanorama` - This option allows a specific panorama to always be displayed. For example
  using `forcedPanorama = "panoramas:frozen_ocean"` will ignore all other entries and only show the frozen ocean
  panorama.

## Custom Panoramas

This mod can load custom panoramas from resource packs. This allows mods and modpacks to add their own panoramas that
can randomly show up on the title menu while the player plays the game. The intended goal of this mod is to create a
system where multiple projects can share the spotlight rather than fighting over which mod gets to display their
panoramas.

### Creating New Panoramas

Panoramas care loaded using resource packs. This can be any type of resource pack, such as a mod's resource pack, a user
applied resource pack, or even a modpack defined resource pack. The panorama files are loaded from
the `assets/%namespace%/panoramas/` folder, where the namespace is a lowercase ID for the project that is adding the new
panoramas. A mod will use their modid, while a modpack may use their name. In this tutorial we will
use `assets/examples/panoramas/`

The first step to adding a panorama is to create a panorama JSON file. This file goes in the panoramas folder, and the
name of the file determines the ID of the panorama. Like all resource pack files this should be a lower case file with
no special characters other than `_`. In this example we will use the file `my_panorama.json`. This means our full
panorama ID will be `examples:my_panorama`.

Inside the panorama JSON file you can configure several properties which will change the behaviour of your panorama.
None of these properties are required.

| Property   | Example                                         | Required | Description                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      |
|------------|-------------------------------------------------|----------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| type       | panoramas:vanilla                               | false    | This determines the type of panorama being created. By default the mod only supports vanilla-like panoramas however mods can add new types.                                                                                                                                                                                                                                                                                                                                                                                                                                                      |
| conditions | N/A                                             | false    | An array of load conditions can be defined. These use the Forge recipe load conditions and can be used to prevent files from loading unless a specific mod is present.                                                                                                                                                                                                                                                                                                                                                                                                                           |
| texture    | examples:textures/gui/title/background/panorama | false    | The resource path to the texture you want to use. A vanilla-like panorama will look for 6 textures suffixed _0 to _5 where each texture forms a different part of the cube. By default this texture will be taken from ID of the panorama and does not need to be specified manually. If your ID is `examples:my_panorama` it will look for `assets/examples/textures/gui/title/background/my_panorama_0` and so on.                                                                                                                                                                             |
| weight     | 200                                             | false    | The weight is used to determine how likely a panorama is to be chosen when selecting a random panorama. The higher the value the more likely it is to be selected. The percent chance is determined by dividing the weight by the combined weight of all loaded panoramas. For example if there are two panoramas with a weight of 100 they would each have a 50% chance to display. If one is 100 and the other is 300, the one with 300 weight would show up 75% of the time while the one with 100 weight would show up 25%. The vanilla panorama weight is 1000. The default weight is 1000. |

### Examples

#### Simple Panorama - Default Properties

This example uses all default properties and is effectively an empty file, except for the two curly brackets
which are required for a JSON file to be valid. If the file was in `assets/examples/panoramas/default.json` it would
create a simple panorama using the textures from `assets/examples/textures/gui/title/background/default_0.png`
to `assets/examples/textures/gui/title/background/default_5.png`. The weight would be 1000 making it as likely to appear
as the vanilla panorama.

```json
{
}
```

#### Configured Panorama

This example sets most of the available properties. It has a weight of 2000, making it twice as common as the default
vanilla panorama. It will use panorama textures from `assets/examples/textures/panoramas/example_0.png`
to `assets/examples/textures/panoramas/example_5.png`.

```json
{
  "type": "panoramas:vanilla",
  "texture": "examples:textures/panoramas/example",
  "weight": 2000
}
```

#### Conditional Panorama

Sometimes you may only want a panorama to load under certain conditions, such as a specific mod being installed and
available. This can be achieved using Forge's recipe conditions system. For example imagine we want a panorama to only
load when the mod buildcraft is installed. We can use the following approach.

```json
{
  "conditions": [
    {
      "type": "forge:mod_loaded",
      "modid": "buildcraft"
    }
  ],
  "weight": 2000
}
```