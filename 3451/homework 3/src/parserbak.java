import processing.core.*;
import java.util.*;

public class parserbak extends PApplet {

	// Constants
	private final float BIGFLOAT = 50000.0f;
	private Color WHITE = new Color(1.0f, 1.0f, 1.0f);
	private Color BLACK = new Color(0.0f, 0.0f, 0.0f);
	private final int width = 600;
	private Vertex3 ORIGIN = new Vertex3(0, 0, 0);
	private final float SMALLFLOAT = 0.001f;
	
	// A global variable for holding current active file name.
	String gCurrentFile = new String("rect_test.cli");
	
	// Global variable for holding the objects in this scene
	ArrayList<RayTracableObject> sceneObjects = new ArrayList<RayTracableObject>();
	
	// Global variable for holding the lights in this scene
	ArrayList<RayTracableLight> sceneLights = new ArrayList<RayTracableLight>();
	
	// Global variable for holding the material information
	RayTracableMaterial currentMaterial = new RayTracableMaterial(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
	
	// Global variable for holding the background color
	Color backgroundColor = BLACK;
	
	// Global variable holding the field of view angle.
	float fovAngle = radians(90.0f);
	
	public void keyPressed() {
		switch(key) {
			case '1':  gCurrentFile = new String("t0.cli"); interpreter(); break;
			case '2':  gCurrentFile = new String("t1.cli"); interpreter(); break;
			case '3':  gCurrentFile = new String("t2.cli"); interpreter(); break;
			case '4':  gCurrentFile = new String("t3.cli"); interpreter(); break;
			case '5':  gCurrentFile = new String("c0.cli"); interpreter(); break;
			case '6':  gCurrentFile = new String("c1.cli"); interpreter(); break;
			case '7':  gCurrentFile = new String("c2.cli"); interpreter(); break;
			case '8':  gCurrentFile = new String("c3.cli"); interpreter(); break;
			case '9':  gCurrentFile = new String("c4.cli"); interpreter(); break;
			case '0':  gCurrentFile = new String("c5.cli"); interpreter(); break;
		}
	}
	
	/**
	 * Gets a float value from a string.
	 */
	float get_float(String str) { 
		return Float.parseFloat(str); 
	}
	
	/**
	 * Parser core. It parses the CLI file and processes it based on each
	 * token. Only "color", "rect", and "write" tokens are implemented.
	 * You should start from here and add more functionalities for your
	 * ray tracer.
	 * Note: Function "splitToken()" is only available in processing 1.25
	 * or higher.
	 */
	void interpreter() {
		loadDefaults();
		String str[] = loadStrings(gCurrentFile);
		if (str == null) println("Error! Failed to read the file.");
		for (int i=0; i<str.length; i++) {
			String[] token = splitTokens(str[i], " "); // Get a line and parse tokens.
			if (token.length == 0) 
				continue; // Skip blank line.
			if(!token[0].equals("#"))
				println(token[0]);
			if (token[0].equals("fov")) {
				changeFov(token);
			} else if (token[0].equals("background")) {
				changeBackground(token);
			} else if (token[0].equals("light")) {
				createLight(token);
			} else if (token[0].equals("surface")) {
				changeSurface(token);
			} else if (token[0].equals("cyl")) {
				createCylinder(token);
			} else if (token[0].equals("sphere")) {
				createSphere(token);
			} else if (token[0].equals("color")) {
				changeColor(token);
			} else if (token[0].equals("rect")) {
				createRectangle(token);
			} else if (token[0].equals("write")) {
				rayTrace();
				save(token[1]);  
			}
		}
	}

	private void loadDefaults() {
		sceneObjects = new ArrayList<RayTracableObject>();
		sceneLights = new ArrayList<RayTracableLight>();
		currentMaterial = new RayTracableMaterial(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
		backgroundColor = BLACK;
		fovAngle = radians(90.0f);
	}

	/**
	 * Where the actual raytracing takes place!
	 */
	private void rayTrace() {
		loadPixels();
		
		float z = -1 * (width / 2) / (tan(fovAngle / 2));
		for(int x = 0; x < width; x++){
			for(int y = 0; y < width; y++){
				Vertex3 ray = (new Vertex3(-width / 2.0f + x, width / 2.0f - y, z)).normalize();
				TraceResult result = TraceRay(ORIGIN, ray);
				if(result != null){
//					println(String.format("Pixel (%d, %d) intersects object %s at %s", x, y, result.intersectedObject, result.intersectionLocation));
					RayTracableMaterial material = result.intersectedObject.material();
					Color color = material.ambient;
					for(RayTracableLight light : sceneLights){
						// c = cr(ca + cl * max(0, n.l)) + cl(h.n)^p
						Color cr = material.diffuse;
						Color ca = material.ambient;
						Color cl = material.specular;
						Color lightColor = light.color;
						float phong = material.phong;
						Vertex3 n = result.normal;
						Vertex3 e = result.intersectionLocation.times(-1).normalize();
						Vertex3 l = light.location.minus(result.intersectionLocation).normalize();
						Vertex3 h = e.plus(l).normalize();
						TraceResult r2 = TraceRay(result.intersectionLocation, l);
						if(r2 == null || r2.intersectedObject == result.intersectedObject){
//						if(r2 == null){
							Color diffuse = lightColor.times(cr.times(max(0, n.dotProd(l))));
							Color specular = lightColor.times(cl.times(max(0, pow(h.dotProd(n), phong))));
							color = color.plus(diffuse).plus(specular);
						}
					}
					pixels[x + y * width] = color.toProcessingColor();
				} else {
					pixels[x + y * width] = backgroundColor.toProcessingColor();
				}
			}
		}
		updatePixels();
	}
	
	public class Color {
		float r, g, b;
		public Color(float r, float g, float b){
			this.r = r; this.g = g; this.b = b;
		}
		public Color(){
			this.r = 1.0f; this.g = 1.0f; this.b = 1.0f;
		}
		public int toProcessingColor(){
			return color(r, g, b);
		}
		public Color plus(Color c){
			return new Color(min(1, c.r + r), min(1, c.g + g), min(1, c.b + b));
		}
		public Color times(Color c){
			return new Color(min(1, c.r * r), min(1, c.g * g), min(1, c.b * b));
		}
		public Color times(float f){
			return new Color(min(1, r * f), min(1, g * f), min(1, b * f));
		}
	}
	
	public TraceResult TraceRay(Vertex3 source, Vertex3 direction){
		Vertex3 nearestPoint = null;
		Vertex3 normal = null;
		RayTracableObject nearestObject = null;
		for(RayTracableObject sceneObj : sceneObjects){
			ArrayList<Vertex3> intersectPoints = sceneObj.IntersectsRay(source, direction);
			if(intersectPoints != null){
				for(Vertex3 point : intersectPoints){
					if(nearestPoint == null || point.minus(source).length() < nearestPoint.minus(source).length()){
						if(point.minus(source).length() > SMALLFLOAT){
							nearestPoint = point;
							nearestObject = sceneObj;
							normal = sceneObj.NormalAt(point);
						}
					}
				}
			}
		}
		if(nearestPoint != null){
			return new TraceResult(nearestPoint, normal, nearestObject);
		} else {
			return null;
		}
	}
	
	public class TraceResult {
		Vertex3 intersectionLocation;
		Vertex3 normal;
		RayTracableObject intersectedObject;
		public TraceResult(Vertex3 intersection, Vertex3 normal, RayTracableObject object){
			this.intersectionLocation = intersection;
			this.normal = normal;
			this.intersectedObject = object;
		}
	}
	
	private void createRectangle(String[] token) {
		float x0 = get_float(token[1]);
		float y0 = get_float(token[2]);
		float x1 = get_float(token[3]);
		float y1 = get_float(token[4]);
		rect(x0, height-y1, x1-x0, y1-y0);
	}

	private void changeColor(String[] token) {
		float r = get_float(token[1]);
		float g = get_float(token[2]);
		float b = get_float(token[3]);
		fill(r, g, b);
	}

	private void createSphere(String[] token) {
		float x = get_float(token[1]);
		float y = get_float(token[2]);
		float z = get_float(token[3]);
		float radius = get_float(token[4]);
		RayTracableSphere sphere = new RayTracableSphere(x, y, z, radius, currentMaterial);
		sceneObjects.add(sphere);
	}

	private void createCylinder(String[] token) {
		float x = get_float(token[1]);
		float y = get_float(token[2]);
		float z = get_float(token[3]);
		float height = get_float(token[4]);
		float radius = get_float(token[5]);
		RayTracableCylinder cylinder = new RayTracableCylinder(x, y, z, height, radius, currentMaterial);
		sceneObjects.add(cylinder);
	}

	private void changeSurface(String[] token) {
		float car = get_float(token[1]);
		float cag = get_float(token[2]);
		float cab = get_float(token[3]);
		float crr = get_float(token[4]);
		float crg = get_float(token[5]);
		float crb = get_float(token[6]);
		float clr = get_float(token[7]);
		float clg = get_float(token[8]);
		float clb = get_float(token[9]);
		float p   = get_float(token[10]);
		float krefl = get_float(token[11]);
		currentMaterial = new RayTracableMaterial(car, cag, cab, crr, crg, crb, clr, clg, clb, p, krefl);
	}

	private void createLight(String[] token) {
		float x = get_float(token[1]);
		float y = get_float(token[2]);
		float z = get_float(token[3]);
		float r, g, b;
		if(token.length > 4){
			r = get_float(token[4]);
			g = get_float(token[5]);
			b = get_float(token[6]);
		} else {
			r = 1; g = 1; b = 1;
		}
		RayTracableLight light = new RayTracableLight(x, y, z, r, g, b);
		sceneLights.add(light);
	}

	private void changeBackground(String[] token) {
		float r = get_float(token[1]);
		float g = get_float(token[2]);
		float b = get_float(token[3]);
		backgroundColor = new Color(r, g, b);
	}

	public void changeFov(String[] token) {
		float angle = get_float(token[1]);
		fovAngle = radians(angle);
	}
	
	public class Vertex3 {
		public float x, y, z;
		public Vertex3(){
			x = 1; y = 1; z = 1;
		}
		public Vertex3(float x, float y, float z){
			this.x = x; this.y = y; this.z = z;
		}
		
		/**
		 * Returns a Vertex that equals this - b.
		 */
		public Vertex3 minus(Vertex3 b){
			return new Vertex3(this.x - b.x, this.y - b.y, this.z - b.z);
		}
		
		public Vertex3 times(float k){
			return new Vertex3(this.x * k, this.y * k, this.z * k);
		}
		
		/**
		 * Returns a Vertex that equals this + b.
		 */
		public Vertex3 plus(Vertex3 b){
			return new Vertex3(this.x + b.x, this.y + b.y, this.z + b.z);
		}
		
		/**
		 * Length of this vertex.
		 */
		public float length(){
			return sqrt(dotProd(this));
		}
		
		public Vertex3 divide(float d){
			return new Vertex3(x / d, y / d, z / d);
		}
		
		public Vertex3 normalize(){
			return this.divide(length());
		}
		
		public float dotProd(Vertex3 v){
			return this.x * v.x + this.y * v.y + this.z * v.z;
		}
		
		public float squared(){
			return dotProd(this);
		}
		
		public String toString(){
			return String.format("(%.2f, %.2f, %.2f)", x, y, z);
		}
	}
	
	public interface RayTracableObject {
		/**
		 * Ray intersection algorithm. If the given Vertex (ray) intersects the object, then
		 * an ArrayList of Vertex's will be returned of where the ray intersects the object.
		 * If the given Vertex does not intersect the object, then null is returned.
		 * @param v Vertex to test for intersection.
		 * @return ArrayList of intersection points if intersected, null otherwise.
		 */
		public ArrayList<Vertex3> IntersectsRay(Vertex3 source, Vertex3 direction);
		
		/**
		 * Get the material for this object.
		 * @return
		 */
		public RayTracableMaterial material();
		
		/**
		 * Get the normal at a given location.
		 */
		public Vertex3 NormalAt(Vertex3 v);
	}
	
	/**
	 * A sphere primitive to be used for ray tracing.
	 */
	public class RayTracableSphere implements RayTracableObject {
		public Vertex3 center;
		public float radius;
		public RayTracableMaterial surfaceMaterial;
		
		/**
		 * Constructor for a sphere to be used with ray tracing.
		 * @param x The x-coordinate for the center of the sphere.
		 * @param y The y-coordinate for the center of the sphere.
		 * @param z The z-coordinate for the center of the sphere.
		 * @param radius The Radius of the sphere.
		 */
		public RayTracableSphere(float x, float y, float z, float radius, RayTracableMaterial material){
			this.center = new Vertex3(x, y, z);
			this.radius = radius; 
			this.surfaceMaterial = material.clone();
		}
		
		/**
		 * Determines if a given ray intersects this sphere.
		 * Returns all vectors (absolute location) that intersect this sphere.
		 * Returns null if the given vector does not intersect this sphere.
		 */
		public ArrayList<Vertex3> IntersectsRay(Vertex3 source, Vertex3 direction) {
			ArrayList<Vertex3> results = null;
			// for comments, read . as "dot product"
			// v = source - center
			Vertex3 v = source.minus(center);
			float vDotDirection = v.dotProd(direction);
			// discriminant: (v.d)^2 - (v.v) + radius^2
			float discriminant = vDotDirection * vDotDirection - v.dotProd(v) + radius * radius;
			if(discriminant >= 0){
				results = new ArrayList<Vertex3>();
				float t = -1 * vDotDirection + sqrt(discriminant);
				Vertex3 r = direction.times(t).plus(source);
				if(t > 0)
					results.add(r);
				if(discriminant != 0){
					t = -1 * vDotDirection - sqrt(discriminant);
					r = direction.times(t).plus(source);
					if(t > 0)
						results.add(r);
				}
			}
			return results;
		}

		@Override
		public RayTracableMaterial material() {
			return surfaceMaterial;
		}

		@Override
		public Vertex3 NormalAt(Vertex3 v) {
			return v.minus(center).normalize();
		}
		
		public String toString(){
			return String.format("<%.2f, %.2f, %.2f> w/radius: %.2f", center.x, center.y, center.z, radius);
		}
	}

	/**
	 * A cylinder primitive to be used for ray tracing.
	 */
	public class RayTracableCylinder implements RayTracableObject {
		public Vertex3 loc;
		public float radius, height;
		RayTracableMaterial surfaceMaterial;
		
		public RayTracableCylinder(float x, float y, float z, float height, float radius, RayTracableMaterial material){
			this.loc = new Vertex3(x, y, z);
			this.radius = radius; this.height = height; this.surfaceMaterial = material.clone();
		}
		
		@Override
		public ArrayList<Vertex3> IntersectsRay(Vertex3 source, Vertex3 direction) {
			ArrayList<Vertex3> results = new ArrayList<Vertex3>();
			float a, b, c, discrim, t, x, y, z;
			Vertex3 tmp;
			// sides
			// TODO
			
			// caps
			// intersection with planes loc.y and loc.y + height
			// bottom
			t = (loc.y - source.y) / direction.y;
			if(t > 0){
				x = source.x + t * direction.x;
				z = source.z + t * direction.z;
				tmp = new Vertex3(x, loc.y, z);
				a = pow(tmp.x - loc.x, 2) + pow(tmp.z - loc.z, 2) - radius * radius;
				if(a <= 0){
					results.add(tmp);
				}
			}
			// top
			t = (loc.y + height - source.y) / direction.y;
			if(t > 0){
				x = source.x + t * direction.x;
				z = source.z + t * direction.z;
				tmp = new Vertex3(x, loc.y + height, z);
				a = pow(tmp.x - loc.x, 2) + pow(tmp.z - loc.z, 2) - radius * radius;
				if(a <= 0){
					results.add(tmp);
				}
			}
			return results;
		}

		@Override
		public RayTracableMaterial material() {
			return surfaceMaterial;
		}

		@Override
		public Vertex3 NormalAt(Vertex3 v) {
			Vertex3 norm = null;
			if(abs(v.y - loc.y) < SMALLFLOAT){
				norm = new Vertex3(0, -1, 0);
			} else if(abs(v.y - (loc.y + height)) < SMALLFLOAT){
				norm = new Vertex3(0, 1, 0);
			} else {
				norm = new Vertex3(v.x - loc.x, 0, v.z - loc.z);
//				norm = norm.times(-1);
			}
			return norm.normalize();
		}
		
		public String toString(){
			return String.format("Cylinder loc: <%.2f, %.2f, %.2f> height: %.2f radius: %.2f", loc.x, loc.y, loc.z, height, radius);
		}
	}
	
	public class RayTracableMaterial {
		public float phong, reflect;
		public Color ambient, diffuse, specular;
		public RayTracableMaterial(float car, float cag, float cab, float crr, float crg, float crb, float clr, float clg, float clb, float phong, float krefl){
			this.phong = phong; this.reflect = krefl;
			this.ambient = new Color(car, cag, cab);
			this.diffuse = new Color(crr, crg, crb);
			this.specular = new Color(clr, clg, clb);
		}
		
		public RayTracableMaterial(Color ambient, Color diffuse, Color specular, float phong, float krefl){
			this.ambient = ambient; this.diffuse = diffuse; this.specular = specular;
			this.phong = phong; this.reflect = krefl;
		}
		public RayTracableMaterial clone(){
			return new RayTracableMaterial(ambient, diffuse, specular, phong, reflect);
		}
	}
	

	public class RayTracableLight {
		Vertex3 location;
		Color color;
		
		public RayTracableLight(){
			init(0, 0, 0, WHITE);
		}
		
		public RayTracableLight(float x, float y, float z, float r, float g, float b){
			init(x, y, z, new Color(r, g, b));
		}
		
		public RayTracableLight(Vertex3 v){
			init(v.x, v.y, v.z, WHITE);
		}
		
		public RayTracableLight(Vertex3 v, Color color){
			init(v.x, v.y, v.z, color);
		}
		
		public RayTracableLight(float x, float y, float z, Color color){
			init(x, y, z, color);
		}
		
		public RayTracableLight(Vertex3 v, float r, float g, float b){
			init(v.x, v.y, v.z, new Color(r, g, b));
		}
		
		private void init(float x, float y, float z, Color color){
			this.location = new Vertex3(x, y, z); this.color = color;
		}
	}
	

	
	
	/**
	 * Frame drawing loop
	 */
	public void draw() {
		// do nothing, no animation or anything
	}
	
	/**
	 * Some initializations for the scene.
	 */
	public void setup() {
		size(width, width);  
		noStroke();
		colorMode(RGB, 1.0f);
		background(0, 0, 0);
		interpreter();
	}
}
