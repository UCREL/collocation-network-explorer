/*
 * May 29, 2005
 */
package edu.gullick.physics2D;

import java.util.*;

public class ParticleSystem
{
	public static final int RUNGE_KUTTA = 0;
	public static final int MODIFIED_EULER = 1;
	public static final int EULER = 2;
	
	protected static final float DEFAULT_GRAVITY = 0;
	protected static final float DEFAULT_DRAG = 0.001f;	
	
  public Vector<Particle> particles;
  public Vector<Spring> springs;
  public Vector<Attraction> attractions;
  public Vector<Force> customForces = new Vector<Force>();
 
  Integrator integrator;
  
  Vector2D gravity;
  float drag;

  boolean hasDeadParticles = false;
  
  public final void setIntegrator( int integrator )
  {
	switch ( integrator )
	{
		case RUNGE_KUTTA:
			this.integrator = new RungeKuttaIntegrator( this );
			break;
		case MODIFIED_EULER:
			this.integrator = new ModifiedEulerIntegrator( this );
			break;
		case EULER:
			this.integrator = new EulerIntegrator( this );
			break;
	}
  }
  
  public final void setGravity( float x, float y, float z )
  {
	  gravity.set( x, y);
  }
  
  // default down gravity
  public final void setGravity( float g )
  {
	  gravity.set( 0, g);
  }
  
  public final void setDrag( float d )
  {
	  drag = d;
  }
  
  public final void tick()
  {
	  tick( 1 );
  }
  
  public final void tick( float t )
  {  
	  integrator.step( t );
  }
  
  public final Particle makeParticle( float mass, float x, float y, float z )
  {
	  Particle p = new Particle( mass );
	  p.position().set( x, y);
	  particles.add( p );
	  return p;
  }
  
  public final Particle makeParticle()
  {  
	  return makeParticle( 1.0f, 0f, 0f, 0f );
  }
  
  public final Spring makeSpring( Particle a, Particle b, float ks, float d, float r )
  {
	  Spring s = new Spring( a, b, ks, d, r );
	  springs.add( s );
	  return s;
  }
  
  public final Attraction makeAttraction( Particle a, Particle b, float k, float minDistance )
  {
	  Attraction m = new Attraction( a, b, k, minDistance );
	  attractions.add( m );
	  return m;
  }
  
  public final void clear()
  {
	  particles.clear();
	  springs.clear();
	  attractions.clear();
  }
  
  public ParticleSystem( float g, float somedrag )
  {
	integrator = new RungeKuttaIntegrator( this );
    particles = new Vector<Particle>();
    springs = new Vector<Spring>();
    attractions = new Vector<Attraction>();
    gravity = new Vector2D( 0, g);
    drag = somedrag;
  }
  
  public ParticleSystem( float gx, float gy, float gz, float somedrag )
  {
	integrator = new RungeKuttaIntegrator( this );
     particles = new Vector<Particle>();
    springs = new Vector<Spring>();
    attractions = new Vector<Attraction>();
    gravity = new Vector2D( gx, gy);
    drag = somedrag;
  }
  
  public ParticleSystem()
  {
	integrator = new RungeKuttaIntegrator( this );
    particles = new Vector<Particle>();
    springs = new Vector<Spring>();
    attractions = new Vector<Attraction>();
	gravity = new Vector2D( 0, ParticleSystem.DEFAULT_GRAVITY);
	drag = ParticleSystem.DEFAULT_DRAG;
  }
  
  protected final void applyForces()
  {
	  if ( !gravity.isZero() )
	  {
		  for ( int i = 0; i < particles.size(); ++i )
		  {
			  Particle p = particles.get( i );
			  p.force.add( gravity );
		  }
	  }
	
	  for ( int i = 0; i < particles.size(); ++i )
	  {
		  Particle p = (particles.get( i ));
		  p.force.add( p.velocity.x() * -drag, p.velocity.y() * -drag);
	  }
	  
	  for ( int i = 0; i < springs.size(); i++ )
	  {
		  Spring f = springs.get( i );
		  f.apply();
	  }
	  
	  for ( int i = 0; i < attractions.size(); i++ )
	  {
		  Attraction f = attractions.get( i );
		  f.apply();
	  }
	  
	  for ( int i = 0; i < customForces.size(); i++ )
	  {
		  Force f = customForces.get( i );
		  f.apply();
	  }
  }
  
  protected final void clearForces()
  {
	  Iterator<Particle> i = particles.iterator();
	  while ( i.hasNext() )
	  {
		  Particle p = (Particle) i.next();
		  p.force.clear();
	  }
  }
  
  public final int numberOfParticles()
  {
	  return particles.size();
  }
  
  public final int numberOfSprings()
  {
	  return springs.size();
  }
  
  public final int numberOfAttractions()
  {
	  return attractions.size();
  }
  
  public final Particle getParticle( int i )
  {
	  return particles.get( i );
  }
  
  public final Spring getSpring( int i )
  {
	  return springs.get( i );
  }
  
  public final Attraction getAttraction( int i )
  {
	  return attractions.get( i );
  }
  
  public final void addCustomForce( Force f )
  {
	  customForces.add( f );
  }
  
  public final int numberOfCustomForces()
  {
	  return customForces.size();
  }
  
  public final Force getCustomForce( int i )
  {
	  return customForces.get( i );
  }
  
  public final Force removeCustomForce( int i )
  {
	  return customForces.remove( i );
  }
  
  public final void removeParticle( Particle p )
  {
	  particles.remove( p );
  }
  
  public final Spring removeSpring( int i )
  {
	  return springs.remove( i );
  }
  
  public final Attraction removeAttraction( int i  )
  {
	 return attractions.remove( i );
  }
  
  public final void removeAttraction( Attraction s )
  {
	  attractions.remove( s );
  }
  
  public final void removeSpring( Spring a )
  {
	  springs.remove( a );
  }
  
  public final void removeCustomForce( Force f )
  {
	  customForces.remove( f );
  }
  
}
