package club.lyric.loader.stage.antidump.impl;

import club.lyric.loader.stage.antidump.AntiDump;
import club.lyric.loader.utils.BytecodeUtils;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodNode;

import java.util.List;

public class InstrumentationDisabler extends AntiDump {

    @Override
    protected void execute() throws Throwable {
        String className = "sun/instrument/InstrumentationImpl";

        byte[] bytecode = generateClassBytecode(className);

        Class<?> fakeClass = defineClass(className, bytecode);

        Object instance = fakeClass.newInstance();
    }

    private static Class<?> defineClass(String className, byte[] bytecode) {
        MemoryClassLoader classLoader = new MemoryClassLoader();
        return classLoader.defineClass(className, bytecode);
    }

    static class MemoryClassLoader extends ClassLoader {
        public Class<?> defineClass(String name, byte[] b) {
            return defineClass(name, b, 0, b.length);
        }
    }

    private byte[] generateClassBytecode(String className) {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        cw.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC + Opcodes.ACC_SUPER, className, null, "java/lang/Object", null);

        MethodVisitor staticBlock = cw.visitMethod(Opcodes.ACC_STATIC, "<clinit>", "()V", null, null);
        staticBlock.visitCode();

        List<MethodNode> methodNodes = BytecodeUtils.staticCrasher("InstrumentationImpl static block initialized");
        for (MethodNode methodNode : methodNodes) {
            methodNode.accept(staticBlock);
        }

        staticBlock.visitInsn(Opcodes.RETURN);
        staticBlock.visitMaxs(0, 0);
        staticBlock.visitEnd();

        cw.visitEnd();

        return cw.toByteArray();
    }
}
