# Infinity
````
Infinity is a 1.20.6 client, made by lyric and Valser, with a custom event system thanks to RailHack.
The client was made to learn how to develop on the Fabric platform, and newer versions of Minecraft.
The client is not intended to be used, as none of the people with access to it play the game, making it more of a learning experience.
The client's design philosophy reflects that of a 'side' client, ie it is intended to be used alongside another client such as Future.

The client has several ways that it is optimised:
- Using mixins to handle certain reoccuring events rather than the EventBus to reduce latency (see OtherEntityManager for examples)
- Having a clearly defined structure for modules, instead of throwing everything in Utils
- Having a simple setting system
- Having a custom EventBus that performs extremely well (thanks to RailHack)
- Limiting the use of @EventHandler as much as possible
- Making sure most classes are defined as 'final' in order to save JVM memory, and decrease JVM processing time
````
# Credits
````
https://github.com/SpaiR -> Dear-IMGUI java bindings
RailHack -> Custom EventBus, (deprecated) loading system, and teaching me how the JVM handles memory
Discord -> Discord RPC system files
finz0 -> IRC implementation
Steinborn -> LazyDFU optimisation
````
# Contact
``````
https://discord.gg/A4jpYa9fBq
``````

