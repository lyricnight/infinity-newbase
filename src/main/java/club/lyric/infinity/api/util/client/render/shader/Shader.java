package club.lyric.infinity.api.util.client.render.shader;

import club.lyric.infinity.Infinity;
import club.lyric.infinity.api.util.client.render.util.Render2DUtils;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.lwjgl.opengl.ARBShaderObjects;

import java.io.*;

import static org.lwjgl.opengl.ARBShaderObjects.*;
import static org.lwjgl.opengl.GL20.*;

public class Shader implements IMinecraft {

    private final int programID;

    public static Shader
            CORNER_ROUND_SHADER,
            ROUND_SHADER;

    public Shader(String fragmentShaderLoc) {
        programID = ARBShaderObjects.glCreateProgramObjectARB();

        try {
            int fragmentShaderID = switch (fragmentShaderLoc) {
                case "corner" ->
                        createShader(new ByteArrayInputStream(roundedCornerRect.getBytes()), GL_FRAGMENT_SHADER);
                case "round" -> createShader(new ByteArrayInputStream(roundedRect.getBytes()), GL_FRAGMENT_SHADER);
                default ->
                        createShader(mc.getResourceManager().getResource(new Identifier("infinity/shader/" + fragmentShaderLoc)).get().getInputStream(), GL_FRAGMENT_SHADER);
            };
            ARBShaderObjects.glAttachObjectARB(programID, fragmentShaderID);
            ARBShaderObjects.glAttachObjectARB(programID, createShader(new ByteArrayInputStream(vertex.getBytes()), GL_VERTEX_SHADER));
            ARBShaderObjects.glLinkProgramARB(programID);
        } catch (IOException exception) {
            exception.fillInStackTrace();
            Infinity.LOGGER.error("Error while downloading " + fragmentShaderLoc);
        }
    }

    public static void init() {
        CORNER_ROUND_SHADER = new Shader("corner");
        ROUND_SHADER = new Shader("round");
    }

    public void attach() {
        ARBShaderObjects.glUseProgramObjectARB(programID);
    }

    public void detach() {
        glUseProgram(0);
    }

    public void setUniform(String name, float... args) {
        int loc = ARBShaderObjects.glGetUniformLocationARB(programID, name);
        switch (args.length) {
            case 1: {
                ARBShaderObjects.glUniform1fARB(loc, args[0]);
                break;
            }
            case 2: {
                ARBShaderObjects.glUniform2fARB(loc, args[0], args[1]);
                break;
            }
            case 3: {
                ARBShaderObjects.glUniform3fARB(loc, args[0], args[1], args[2]);
                break;
            }
            case 4: {
                ARBShaderObjects.glUniform4fARB(loc, args[0], args[1], args[2], args[3]);
                break;
            }
            default: {
                throw new IllegalArgumentException("Invalid number of args. '" + name + "'");
            }
        }
    }

    public void setUniform(String name, int... args) {
        int loc = ARBShaderObjects.glGetUniformLocationARB(programID, name);
        switch (args.length) {
            case 1: {
                glUniform1iARB(loc, args[0]);
                break;
            }
            case 2: {
                glUniform2iARB(loc, args[0], args[1]);
            }
            case 3: {
                glUniform3iARB(loc, args[0], args[1], args[2]);
                break;
            }
            case 4: {
                glUniform4iARB(loc, args[0], args[1], args[2], args[3]);
            }
            default: {
                throw new IllegalArgumentException("Invalid number of args. '" + name + "'");
            }
        }
    }

    public static void setupRoundedRectUniforms(float x, float y, float width, float height, float radius, Shader shader) {
        shader.setUniform("location", x * 2, (mc.getWindow().getHeight() - (height * 2)) - (y * 2));
        shader.setUniform("rectSize", width * 2, height * 2);
        shader.setUniform("radius", radius * 2);
    }

    public void drawQuads(MatrixStack matrices, float x, float y, float width, float height) {
        Render2DUtils.quadsBegin(matrices, x, y, width, height);
    }

    private int createShader(InputStream inputStream, int shaderType) {
        int shader = ARBShaderObjects.glCreateShaderObjectARB(shaderType);
        ARBShaderObjects.glShaderSourceARB(shader, readInputStream(inputStream));
        ARBShaderObjects.glCompileShaderARB(shader);
        if (ARBShaderObjects.glGetObjectParameteriARB(shader, 35713) == 0) {
            Infinity.LOGGER.info(ARBShaderObjects.glGetInfoLogARB(shader, 4096));
            throw new IllegalStateException(String.format("Shader (%s) failed to compile.", shaderType));
        }
        return shader;
    }

    public static String readInputStream(InputStream inputStream) {
        StringBuilder stringBuilder = new StringBuilder();

        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append('\n');
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }

    String roundedRect = "#version 120\n" +
            "\n" +
            "uniform vec2 location, rectSize;\n" +
            "uniform vec4 color;\n" +
            "uniform float radius;\n" +
            "uniform bool blur;\n" +
            "\n" +
            "float roundSDF(vec2 p, vec2 b, float r) {\n" +
            "    return length(max(abs(p) - b, 0.0)) - r;\n" +
            "}\n" +
            "\n" +
            "\n" +
            "void main() {\n" +
            "    vec2 rectHalf = rectSize * 0.5;\n" +
            "    // Smooth the result (free antialiasing).\n" +
            "    float smoothedAlpha =  (1.0-smoothstep(0.0, 1.0, roundSDF(rectHalf - (gl_TexCoord[0].st * rectSize), rectHalf - radius - 1., radius))) * color.a;\n" +
            "    gl_FragColor = vec4(color.rgb, smoothedAlpha);// mix(quadColor, shadowColor, 0.0);\n" +
            "\n" +
            "}";

    String vertex = "#version 120       \n" +
            "void main() {\n" +
            "    gl_TexCoord[0] = gl_MultiTexCoord0;\n" +
            "    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;\n" +
            "}\n";

    String roundedCornerRect = "#version 120\n" +
            "    uniform vec2 size;\n" +
            "    uniform vec4 round;\n" +
            "    uniform vec2 smoothness;\n" +
            "    uniform float value;\n" +
            "    uniform vec4 color; // цвет прямоугольника\n" +
            "\n" +
            "    float test(vec2 vec_1, vec2 vec_2, vec4 vec_4) {\n" +
            "        vec_4.xy = (vec_1.x > 0.0) ? vec_4.xy : vec_4.zw;\n" +
            "        vec_4.x = (vec_1.y > 0.0) ? vec_4.x : vec_4.y;\n" +
            "        vec2 coords = abs(vec_1) - vec_2 + vec_4.x;\n" +
            "        return min(max(coords.x, coords.y), 0.0) + length(max(coords, vec2(0.0f))) - vec_4.x;\n" +
            "    }\n" +
            "\n" +
            "\n" +
            "    void main() {\n" +
            "        vec2 st = gl_TexCoord[0].st * size; // координаты текущего пикселя\n" +
            "        vec2 halfSize = 0.5 * size; // половина размера прямоугольника\n" +
            "        float sa = 1.0 - smoothstep(smoothness.x, smoothness.y, test(halfSize - st, halfSize - value, round));\n" +
            "        gl_FragColor = mix(vec4(color.rgb, 0.0), vec4(color.rgb, color.a), sa); // устанавливаем цвет прямоугольника с прозрачностью sa\n" +
            "    }";
}