# Infinity
````
Infinity is a 1.21.4 client, made by lyric and Valser, with a custom event system thanks to RailHack.
The client was made to learn how to develop on the Fabric platform, and newer versions of Minecraft.
The client is not intended to be used, as none of the people with access to it play the game, making it more of a learning experience.
The client's design philosophy reflects that of a 'side' client, ie it is intended to be used alongside another client such as Future.

The client has several ways that it is optimised:
- Using Lombok to automatically generate generic methods at runtime rather than having a bunch of bloated code
- Using mixins to handle certain reoccuring events rather than the EventBus to reduce latency (see OtherEntityManager for examples)
- Having a clearly defined structure for modules, instead of throwing everything in Utils
- Having a simple setting system
- Having a custom EventBus that performs extremely well (thanks to RailHack)
- Limiting the use of @EventHandler as much as possible
- Making sure most classes are defined as 'final' in order to save JVM memory, and decrease JVM processing time
````
# Credits
````
https://projectlombok.org/ -> Project Lombok
https://github.com/SpaiR -> Dear-IMGUI java bindings
RailHack -> Custom EventBus, (deprecated) loading system, and teaching me how the JVM handles memory
Discord -> Discord RPC system files
finz0 -> IRC implementation
https://github.com/astei/lazydfu -> LazyDFU optimisation (deprecated)
````
# Contact
``````
https://discord.gg/A4jpYa9fBq
``````

