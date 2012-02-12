package com.bitfire.uracer.tiled;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.materials.Material;
import com.badlogic.gdx.graphics.g3d.materials.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.model.still.StillModel;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.bitfire.uracer.Director;
import com.bitfire.uracer.utils.Convert;

/**
 * The model is expected to follow the z-up convention.
 *
 * @author manuel
 *
 */
public class OrthographicAlignedStillModel
{
	public UStillModel model;
	public Material material;
	public boolean isTransparent = false;
	protected Texture texture;
	protected TextureAttribute textureAttribute;

	public static ShaderProgram shaderProgram = null;

	// Blender => cube 14.2x14.2 meters = one tile (256px) w/ far plane @48
	// (256px are 14.2mt w/ 18px/mt)
	// I'm lazy and want Blender to work with 10x10mt instead, so a 1.42f
	// factor for this scaling: also, since the far plane is suboptimal at
	// just 48, i want 5 times more space on the z-axis, so here's another
	// scaling factor creeping up.
	protected static float BlenderToURacer = 5f * 1.42f;

	// scale
	private float scale, scalingFactor;
	public Vector3 scaleAxis = new Vector3();

	// position
	public Vector2 positionOffsetPx = new Vector2( 0, 0 );
	public Vector2 positionPx = new Vector2();


	// explicitle initialize the static iShader member
	// (Android: statics need to be re-initialized!)
	public static void initialize()
	{
		String vertexShader =
				"uniform mat4 u_mvpMatrix;					\n" +
				"attribute vec4 a_position;					\n" +
				"attribute vec2 a_texCoord0;				\n" +
				"varying vec2 v_TexCoord;					\n" +
				"void main()								\n" +
				"{											\n" +
				"	gl_Position = u_mvpMatrix * a_position;	\n" +
				"	v_TexCoord = a_texCoord0;				\n" +
				"}											\n";

		String fragmentShader =
			"#ifdef GL_ES											\n" +
			"precision mediump float;								\n" +
			"#endif													\n" +
			"uniform sampler2D u_texture;							\n" +
			"varying vec2 v_TexCoord;								\n" +
			"void main()											\n" +
			"{														\n" +
			"	gl_FragColor = texture2D( u_texture, v_TexCoord );	\n" +
			"}														\n";

		ShaderProgram.pedantic = false;
		OrthographicAlignedStillModel.shaderProgram = new ShaderProgram( vertexShader, fragmentShader );

		if( OrthographicAlignedStillModel.shaderProgram.isCompiled() == false )
			throw new IllegalStateException( OrthographicAlignedStillModel.shaderProgram.getLog() );
	}

	public OrthographicAlignedStillModel(StillModel aModel, Texture aTexture, boolean transparency)
	{
		try
		{
			model = new UStillModel( aModel.subMeshes.clone() );

			// set material
			texture = aTexture;
			textureAttribute = new TextureAttribute(texture, 0, "textureAttributes");
			material = new Material("default", textureAttribute);
			model.setMaterial( material );

			setScalingFactor( Director.scalingStrategy.meshScaleFactor * BlenderToURacer * Director.scalingStrategy.to256 );
			setPosition( 0, 0 );
			setRotation( 0, 0, 0, 0 );
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}

	public OrthographicAlignedStillModel(StillModel model, Texture texture)
	{
		this(model, texture, false);
	}

	public void dispose()
	{
		try {
			model.dispose();
		} catch( IllegalArgumentException e )
		{
			// buffer already disposed
		}
	}

	public TextureAttribute getTextureAttribute()
	{
		return textureAttribute;
	}

	public void setPositionOffsetPixels( int offsetPxX, int offsetPxY )
	{
		positionOffsetPx.x = offsetPxX;
		positionOffsetPx.y = offsetPxY;
	}

	/*
	 * @param x_index the x-axis index of the tile
	 * @param x_index the y-axis index of the tile
	 *
	 * @remarks The origin (0,0) is at the top-left corner
	 */
	public void setTilePosition( int tileIndexX, int tileIndexY )
	{
		positionPx.set( Convert.tileToPx( tileIndexX, tileIndexY ) );
	}

	/**
	 * Sets the world position in pixels, top-left origin.
	 * @param posPxX
	 * @param posPxY
	 */
	public void setPosition( float posPxX, float posPxY )
	{
		positionPx.set( Director.positionFor( posPxX, posPxY ) );
	}

	/**
	 * Sets the world position in pixels, top-left origin.
	 * @param x
	 * @param y
	 */
	public void setPositionUnscaled( float x, float y )
	{
		positionPx.set( x, y );
	}

	public float iRotationAngle;
	public Vector3 iRotationAxis = new Vector3();

	public void setRotation( float angle, float x_axis, float y_axis, float z_axis )
	{
		iRotationAngle = angle;
		iRotationAxis.set( x_axis, y_axis, z_axis );
	}

	public void setScalingFactor( float factor )
	{
		scalingFactor = factor;
		scaleAxis.set( scale, scale, scale );
	}

	public void setScale( float scale )
	{
		this.scale = scalingFactor * scale;
		scaleAxis.set( this.scale, this.scale, this.scale );
	}

	/**
	 * This is for model with submeshes and/or handling transparency correctly.
	 *
	 * NOTE: shaders and texture units are already setup and bound for you, here
	 * goes just plain rendering.
	 */
	public void render(GL20 gl)
	{
		model.subMeshes[0].mesh.render(OrthographicAlignedStillModel.shaderProgram, model.subMeshes[0].primitiveType);
	}
}
