package club.lyric.loader.utils;

import club.lyric.loader.stage.ClientLoader;
import com.google.gson.JsonObject;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.util.ArrayList;
import java.util.List;

public class BytecodeUtils implements Opcodes {

    public static ClassNode newClassNode(String name, String superName) {
        ClassNode newNode = new ClassNode();
        newNode.name = name;
        newNode.superName = superName;
        newNode.version = V1_8;
        newNode.access = ACC_PUBLIC;
        return newNode;
    }

    public static byte[] getBytes(ClassNode node) {
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        node.accept(writer);
        return writer.toByteArray();
    }

    public static List<MethodNode> staticCrasher(String message) {
        JsonObject alert = AlertUtils.getFormattedMessage(ClientLoader.LoadingStage.POSTLOAD, new Throwable(message));
        MethodNode crasher = new MethodNode(ACC_PUBLIC + ACC_STATIC, "<clinit>", "()V", null, null);

        InsnList insns = new InsnList();

        insns.add(new LdcInsnNode("club.lyric.loader.Natives"));
        insns.add(new InsnNode(ICONST_1));
        insns.add(new FieldInsnNode(GETSTATIC, "net/fabricmc/loader/impl/FabricLoaderImpl", "INSTANCE", "net/fabricmc/loader/impl/FabricLoaderImpl/InitHelper;"));
        insns.add(new LdcInsnNode("native8"));
        insns.add(new InsnNode(ICONST_1));
        insns.add(new TypeInsnNode(ANEWARRAY, "java/lang/Class"));
        insns.add(new InsnNode(DUP));
        insns.add(new InsnNode(ICONST_0));
        insns.add(new LdcInsnNode(Type.OBJECT));
        insns.add(new MethodInsnNode(INVOKESTATIC, "java/lang/Class", "forName", "(Ljava/lang/String;)Ljava/lang/Class;", false));
        insns.add(new InsnNode(AASTORE));
        insns.add(new MethodInsnNode(INVOKEVIRTUAL, "java/lang/Class", "getDeclaredMethod", "(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;", false));
        insns.add(new InsnNode(ACONST_NULL));
        insns.add(new InsnNode(ICONST_1));
        insns.add(new TypeInsnNode(ANEWARRAY, "java/lang/Object"));
        insns.add(new InsnNode(DUP));
        insns.add(new InsnNode(ICONST_0));
        insns.add(new LdcInsnNode(alert.toString()));
        insns.add(new InsnNode(AASTORE));
        insns.add(new MethodInsnNode(INVOKEVIRTUAL, "java/lang/reflect/Method", "invoke", "(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;", false));
        insns.add(new InsnNode(POP));
        insns.add(new InsnNode(RETURN));

        crasher.instructions = insns;

        List<MethodNode> methods = new ArrayList<>();
        methods.add(crasher);

        return methods;
    }
}
