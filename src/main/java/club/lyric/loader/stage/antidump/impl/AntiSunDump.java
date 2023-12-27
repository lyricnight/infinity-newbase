package club.lyric.loader.stage.antidump.impl;

import club.lyric.loader.stage.antidump.AntiDump;
import club.lyric.loader.utils.BytecodeUtils;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class AntiSunDump extends AntiDump {

    @Override
    protected void execute() throws Throwable {
        defineClassAndSetProperty("sun.jvm.hotspot.tools.jcore.ClassDump", "ClassDump static block initialized");
        defineClassAndSetProperty("sun.jvm.hotspot.tools.jcore.PackageNameFilter", "PackageNameFilter static block initialized");
        defineClassAndSetProperty("king.david.Infinity", "Hotspot dummy filter class initialized");
    }

    private void defineClassAndSetProperty(String className, String message) throws Throwable {
        ClassNode fakeClass = BytecodeUtils.newClassNode(className.replace("/", "."), "java/lang/Object");
        fakeClass.methods = BytecodeUtils.staticCrasher(message);
        byte[] content = BytecodeUtils.getBytes(fakeClass);

        MethodHandles.Lookup lookup = MethodHandles.lookup();
        Class<?> definedClass = defineClassWithLookup(lookup, className, content);

        System.setProperty("sun.jvm.hotspot.tools.jcore.filter", className);

        Object instance = definedClass.newInstance();
    }

    private Class<?> defineClassWithLookup(MethodHandles.Lookup lookup, String className, byte[] bytecode)
            throws Throwable {
        MethodType mt = MethodType.fromMethodDescriptorString("defineClass", byte[].class.getClassLoader());
        var defineClass = lookup.findSpecial(ClassLoader.class, "defineClass", mt, ClassLoader.class);

        ClassLoader classLoader = getClass().getClassLoader();
        return (Class<?>) defineClass.invokeWithArguments(classLoader, className, bytecode, 0, bytecode.length);
    }

}
