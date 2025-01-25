# Infinity
````
Infinity is a 1.21.1 client, made by lyric and Valser, with a custom event system thanks to RailHack.
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
# Improvements
````
Some ways that the client could be improved (in terms of code quality and performance):
- Remove vasler's Version system as it is simply bloat.
- Better Setting system, using actual enums and parent settings (weird due to imgui) (planned)
- More user-friendly command system (only needed for people who can't read)
- A better util system wherein utils only contained methods that are called multiple times, and are essential (Null.is() being an example) (planned)
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

