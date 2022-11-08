package com.lowdragmc.shimmer.client.shader;

import com.lowdragmc.shimmer.ShimmerConstants;
import net.minecraft.CrashReport;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL43;

import java.nio.FloatBuffer;

/**
 * Shader Storage Buffer Object
 */
public class ShaderSSBO {

	public static boolean support() {
		return GL.getCapabilities().GL_ARB_shader_storage_buffer_object;
	}

	public final int id;
	private boolean inValid = false;

	private static final int BUFFER_TYPE = GL43.GL_SHADER_STORAGE_BUFFER;

	public ShaderSSBO() {
		if (!support()) {
			String errorMessage = "need support for GL_ARB_shader_storage_buffer_object";
			ShimmerConstants.LOGGER.error(errorMessage);
			Minecraft.getInstance().delayCrash(() -> new CrashReport(errorMessage, new IllegalStateException(errorMessage)));
		}
		this.id = GL43.glGenBuffers();
	}

	public void close() {
		if (!inValid) {
			GL30.glDeleteBuffers(id);
			inValid = true;
		}else {
			ShimmerConstants.LOGGER.error("try closing an already closed ShaderStorageBufferObject");
		}
	}

	public void bindBuffer() {
		GL30.glBindBuffer(BUFFER_TYPE, this.id);
	}

	public void unBindBuffer() {
		GL30.glBindBuffer(BUFFER_TYPE, 0);
	}

	/**
	 *
	 * @param programIndex the name of the program
	 * @param storageBlockIndex the storage index in the shader
	 * @param index the globally shared binding index
	 */
	public void bindToShader(int programIndex, int storageBlockIndex, int index) {
		bindBuffer();
		GL43.glShaderStorageBlockBinding(programIndex, storageBlockIndex, index);
		unBindBuffer();
	}

	/**
	 * @param index the globally shared binding index
	 */
	public void bindIndex(int index) {
		bindBuffer();
		GL43.glBindBufferBase(BUFFER_TYPE, index, this.id);
		unBindBuffer();
	}

	public void getSubData(long offset, float[] data) {
		bindBuffer();
		GL43.glGetBufferSubData(BUFFER_TYPE, offset, data);
		unBindBuffer();
	}


	public void createBufferData(long size, int mode) {
		bindBuffer();
		GL30.glBufferData(BUFFER_TYPE, size, mode);
		unBindBuffer();
	}

	public void createBufferData(FloatBuffer data, int mode) {
		bindBuffer();
		GL30.glBufferData(BUFFER_TYPE, data, mode);
		unBindBuffer();
	}

	public void bufferSubData(long offset, FloatBuffer data) {
		bindBuffer();
		GL30.glBufferSubData(BUFFER_TYPE, offset, data);
		unBindBuffer();
	}

	public void bufferSubData(long offset, float[] data) {
		bindBuffer();
		GL30.glBufferSubData(BUFFER_TYPE, offset, data);
		unBindBuffer();
	}

	public void bufferSubData(long offset, int[] data) {
		bindBuffer();
		GL30.glBufferSubData(BUFFER_TYPE, offset, data);
		unBindBuffer();
	}

}
