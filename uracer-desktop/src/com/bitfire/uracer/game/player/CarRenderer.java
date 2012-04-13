package com.bitfire.uracer.game.player;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bitfire.uracer.Art;
import com.bitfire.uracer.carsimulation.CarModel;
import com.bitfire.uracer.entities.EntityRenderState;
import com.bitfire.uracer.utils.Convert;

public class CarRenderer {
	private Sprite facet;
	private Sprite ambientOcclusion;
	private TextureRegion region;

	public CarRenderer( CarModel model, TextureRegion region ) {
		// aspect
		this.region = region;
		facet = new Sprite();
		facet.setRegion( region );
		facet.setSize( Convert.mt2px( model.width ), Convert.mt2px( model.length ) );
		facet.setOrigin( facet.getWidth() / 2, facet.getHeight() / 2 );

		// ambient occlusion
		ambientOcclusion = new Sprite();
		ambientOcclusion.setRegion( Art.carAmbientOcclusion );
		ambientOcclusion.setSize( facet.getWidth(), facet.getHeight() );
		ambientOcclusion.setScale( 2f, 2.3f );
		ambientOcclusion.setOrigin( ambientOcclusion.getWidth() / 2, ambientOcclusion.getHeight() / 2 );
	}

	public Sprite getFacet() {
		return facet;
	}

	public TextureRegion getTextureRegion() {
		return region;
	}

	public void render( SpriteBatch batch, EntityRenderState state ) {
		render( batch, state, 1f );
	}

	public void render( SpriteBatch batch, EntityRenderState state, float opacity ) {
		ambientOcclusion.setPosition( state.position.x - ambientOcclusion.getOriginX(), state.position.y - ambientOcclusion.getOriginY() );
		ambientOcclusion.setRotation( state.orientation );
		ambientOcclusion.draw( batch, 0.65f * opacity );

		facet.setPosition( state.position.x - facet.getOriginX(), state.position.y - facet.getOriginY() );
		facet.setRotation( state.orientation );
		facet.draw( batch, opacity );
	}
}