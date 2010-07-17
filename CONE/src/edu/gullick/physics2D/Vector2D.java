package edu.gullick.physics2D;


public class Vector2D
{
	float x;
	float y;


	public Vector2D( float X, float Y )	{ x = X; y = Y;  }
	public Vector2D()                      				{ x = 0; y = 0;  }
	public Vector2D( Vector2D p )							{ x = p.x; y = p.y; ; }
		

	public final float y()                   				{ return y; }
	public final float x()                   				{ return x; }
	
	public final void setX( float X )           			{ x = X; }
	public final void setY( float Y )           			{ y = Y; }

	
	public final void set( float X, float Y )	{ x = X; y = Y; }
	
	public final void set( Vector2D p )						{ x = p.x; y = p.y; }
	
	public final void add( Vector2D p )          				{ x += p.x; y += p.y; }
	public final void subtract( Vector2D p )					{ x -= p.x; y -= p.y; }
	  
	public final void add( float a, float b )		{ x += a; y += b;  } 
	public final void subtract( float a, float b )		{ x -= a; y -= b;  } 
		  
	public final Vector2D multiplyBy( float f )					{ x *= f; y *= f; return this; }
	
	public final float distanceTo( Vector2D p )  			{ return (float)Math.sqrt( distanceSquaredTo( p ) ); }
	
	public final float distanceSquaredTo( Vector2D p )		{ float dx = x-p.x; float dy = y-p.y;  return dx*dx + dy*dy; }
	
	public final float distanceTo( float x, float y )
	{
		float dx = this.x - x;
		float dy = this.y - y;
		return (float)Math.sqrt( dx*dx + dy*dy  );
	}
	
	public final float dot( Vector2D p )         			{ return x*p.x + y*p.y; }
	public final float length()                 			{ return (float)Math.sqrt( x*x + y*y ); }
	public final float lengthSquared()						{ return x*x + y*y;  }
	  
	public final void clear()                   				{ x = 0; y = 0;  }

	public final String toString()              				{ return new String( "(" + x + ", " + y  + ")" ); }

	//public final Vector2D cross( Vector2D p )
	//{
	//	return new Vector2D( 	x = this.y*p.z - this.z*p.y, 
	//							y = this.x*p.z - this.z*p.x,
	//							z = this.x*p.y - this.y*p.x );
	//}
	
	public boolean isZero()
	{
		return x == 0 && y == 0 ;
	}
	}
