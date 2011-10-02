package com.bitfire.uracer.messager;

import java.util.LinkedList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class Messager
{
	public enum MessageType
	{
		Information, Bad, Good
	}

	public enum MessagePosition
	{
		Top, Middle, Bottom
	}

	public enum MessageSize
	{
		Normal, Big
	}

	// data
	private static Array<LinkedList<Message>> messages;
	private static Array<Message> currents;

	private Messager()
	{
	}

	public static void init()
	{
		currents = new Array<Message>( 3 );
		currents.insert( MessagePosition.Top.ordinal(), null );
		currents.insert( MessagePosition.Middle.ordinal(), null );
		currents.insert( MessagePosition.Bottom.ordinal(), null );

		messages = new Array<LinkedList<Message>>( 3 );
		messages.insert( MessagePosition.Top.ordinal(), new LinkedList<Message>() );
		messages.insert( MessagePosition.Middle.ordinal(), new LinkedList<Message>() );
		messages.insert( MessagePosition.Bottom.ordinal(), new LinkedList<Message>() );
	}

	public static void dispose()
	{
		reset();
	}

	public static boolean isBusy( MessagePosition group )
	{
		return (currents.get( group.ordinal() ) != null);
	}

	public static void reset()
	{
		messages.get( MessagePosition.Top.ordinal() ).clear();
		messages.get( MessagePosition.Middle.ordinal() ).clear();
		messages.get( MessagePosition.Bottom.ordinal() ).clear();

		currents.clear();
		messages.clear();

		init();

		// System.out.println("Messages just got cleaned up.");
	}

	public static void tick()
	{
		update( MessagePosition.Top );
		update( MessagePosition.Middle );
		update( MessagePosition.Bottom );
	}

	private static void update( MessagePosition group )
	{
		LinkedList<Message> msgs = messages.get( group.ordinal() );
		Message msg = currents.get( group.ordinal() );

		// any message?
		if( msg == null && (msgs.peek() != null) )
		{
			// schedule this message to be processed next
			msg = msgs.remove();
			currents.set( group.ordinal(), msg );
		}

		// busy or became busy?
		if( msg != null )
		{
			// start message if needed
			if( !msg.started )
			{
				msg.started = true;
				msg.startMs = System.currentTimeMillis();
				msg.onShow();
			}

			if( !msg.tick() )
			{
				currents.set( group.ordinal(), null );
				return;
			}

			// check if finished
			if( (System.currentTimeMillis() - msg.startMs) >= msg.durationMs && !msg.isHiding() )
			{
				// message should end
				msg.onHide();
			}
		}
	}

	public static void render( SpriteBatch batch )
	{
		if( isBusy( MessagePosition.Top ) ) currents.get( MessagePosition.Top.ordinal() ).render( batch );
		if( isBusy( MessagePosition.Middle ) ) currents.get( MessagePosition.Middle.ordinal() ).render( batch );
		if( isBusy( MessagePosition.Bottom ) ) currents.get( MessagePosition.Bottom.ordinal() ).render( batch );
	}

	public static void show( String message, float durationSecs, MessageType type, MessagePosition position, MessageSize size )
	{
		if( isBusy(position) ) currents.get( position.ordinal() ).onHide();
		enqueue( message, durationSecs, type, position, size );
	}

	public static void enqueue( String message, float durationSecs, MessageType type, MessagePosition position, MessageSize size )
	{
		Message m = new Message( message, durationSecs, type, position, size );
		messages.get( position.ordinal() ).add( m );
	}
}