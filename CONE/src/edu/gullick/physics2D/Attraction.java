// attract positive repel negative

package edu.gullick.physics2D;

public class Attraction implements Force
{
	Particle a;
	Particle b;
	float k;
	boolean on;
	float distanceMin;
	float distanceMinSquared;
	
	public Attraction( Particle a, Particle b, float k, float distanceMin )
	{
		this.a = a;
		this.b = b;
		this.k = k;
		on = true;
		this.distanceMin = distanceMin;
		this.distanceMinSquared = distanceMin*distanceMin;
	}

	protected void setA( Particle p )
	{
		a = p;
	}

	protected void setB( Particle p )
	{
		b = p;
	}

	public final float getMinimumDistance()
	{
		return distanceMin;
	}

	public final void setMinimumDistance( float d )
	{
		distanceMin = d;
		distanceMinSquared = d*d;
	}

	public final void turnOff()
	{
		on = false;
	}

	public final void turnOn()
	{
		on = true;
	}

	public final void setStrength( float k )
	{
		this.k = k;
	}

	public final Particle getOneEnd()
	{
		return a;
	}

	public final Particle getTheOtherEnd()
	{
		return b;
	}

	public void apply()
	{
		if ( on && ( a.isFree() || b.isFree() ) )
		{
			float a2bX = a.position().x() - b.position().x();
			float a2bY = a.position().y() - b.position().y();


			float a2bDistanceSquared = a2bX*a2bX + a2bY*a2bY;

			if ( a2bDistanceSquared < distanceMinSquared )
				a2bDistanceSquared = distanceMinSquared;

			float force = k / a.mass / b.mass / a2bDistanceSquared;
//TODO: above / changed from * -- look into this
			float length = (float)Math.sqrt( a2bDistanceSquared );
			
			// make unit vector
			
			a2bX /= length;
			a2bY /= length;
	
			
			// multiply by force 
			
			a2bX *= force;
			a2bY *= force;
	

			// apply
			
			if ( a.isFree() )
				a.force().add( -a2bX, -a2bY );
			if ( b.isFree() )
				b.force().add( a2bX, a2bY );
		}
	}

	public final float getStrength()
	{
		return k;
	}

	public final boolean isOn()
	{
		return on;
	}

	public final boolean isOff()
	{
		return !on;
	}
}
