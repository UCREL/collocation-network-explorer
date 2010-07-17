
package edu.gullick.physics2D;

public class Particle
{
  protected Vector2D position;
  protected Vector2D velocity;
  protected Vector2D force;
  protected float mass;
  protected float age;
  protected boolean dead;
  
  boolean fixed;
	
  public Particle( float m )
  {
	  position = new Vector2D();
	  velocity = new Vector2D();
	  force = new Vector2D();
	  mass = m;
	  fixed = false;
	  age = 0;
	  dead = false;
  }
  
  /* (non-Javadoc)
 * @see traer.physics.AbstractParticle#distanceTo(traer.physics.Particle)
 */
public final float distanceTo( Particle p )
  {
	  return this.position().distanceTo( p.position() );
  }
  
  /* (non-Javadoc)
 * @see traer.physics.AbstractParticle#makeFixed()
 */
public final void makeFixed()
  {
	  fixed = true;
	  velocity.clear();
  }
  
  /* (non-Javadoc)
 * @see traer.physics.AbstractParticle#isFixed()
 */
public final boolean isFixed()
  {
	  return fixed;
  }
  
  /* (non-Javadoc)
 * @see traer.physics.AbstractParticle#isFree()
 */
public final boolean isFree()
  {
	  return !fixed;
  }
  
  /* (non-Javadoc)
 * @see traer.physics.AbstractParticle#makeFree()
 */
public final void makeFree()
  {
	  fixed = false;
  }
  
  /* (non-Javadoc)
 * @see traer.physics.AbstractParticle#position()
 */
public final Vector2D position()
  {
	  return position;
  }
  
  public final Vector2D velocity()
  {
	  return velocity;
  }
  
  /* (non-Javadoc)
 * @see traer.physics.AbstractParticle#mass()
 */
public final float mass()
  {
	  return mass;
  }
  
  /* (non-Javadoc)
 * @see traer.physics.AbstractParticle#setMass(float)
 */
public final void setMass( float m )
  {
	  mass = m;
  }
  
  /* (non-Javadoc)
 * @see traer.physics.AbstractParticle#force()
 */
public final Vector2D force()
  {
	  return force;
  }
  
  /* (non-Javadoc)
 * @see traer.physics.AbstractParticle#age()
 */
public final float age()
  {
	  return age;
  }
  
  protected void reset()
  {
	  age = 0;
	  dead = false;
	  position.clear();
	  velocity.clear();
	  force.clear();
	  mass = 1f;
  }

}
