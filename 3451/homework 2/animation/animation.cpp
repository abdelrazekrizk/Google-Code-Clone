#include "stdafx.h"
#include <math.h>
#include <stdio.h>
#include <stdlib.h>
#include <glut.h>
#include <time.h>

// Window width and height
int width = 1280;
int height = 1024;

float rotX = 0;
float rotY = 0;
float rotZ = 0;

// various constants - probably should use #define for these instead, oh well
const float worldWidth = 10000.0f;
const float roadWidth = 100.0f;
const float roadStripeRatio = 8.0f;
const float roadStripeWidth = 2.0f;
const float roadStripeLength = roadStripeWidth * roadStripeRatio;
const float roadStripeSpacing = 20.0f;
const float roadSolidStripeMargin = 4.0f;
const float streetLightSpacing = 200.0f;
const float streetLightMargin = 5.0f; // distance from road
const float streetLightHeight = 40.0f;
const float streetLightThickness = 1.0f;
const float streetLightLuminance = 1.0f; //fixme
const float streetLightTheta = 15.0f;
const float streetLightBranchLength = 5.0f;
const float streetLightLampHeight = 5.0f;
const float streetLightLampBottomRadius = 5.0f;
const float streetLightLampTopRadius = 2.0f;

// animation variables
int frame = 0;
float carPos[] = {-1 * worldWidth / 2 + 500, -1 * roadWidth / 4, 0};

void drawTriangle(float* v1, float* v2, float* v3){
	float normal[3];
	// to find the normal to this triangle need to calculate a cross b, where
	// a = v1 - v2
	float a[3];
	a[0] = v1[0] - v2[0];
	a[1] = v1[1] - v2[1];
	a[2] = v1[2] - v2[2]; 
	// b = v2 - v3
	float b[3];
	b[0] = v2[0] - v3[0];
	b[1] = v2[1] - v3[1];
	b[2] = v2[2] - v3[2];
	// cross product: axb = <ay*bz - az*by, az*bx - ax*bz, ax*by - ay*bx>
	normal[0] = a[1] * b[2] - a[2] * b[1];
	normal[1] = a[2] * b[0] - a[0] * b[2];
	normal[2] = a[0] * b[1] - a[1] * b[0];
	// normalize to a unit vector
	float length = sqrtf(normal[0] * normal[0] + normal[1] * normal[1] + normal[2] * normal[2]);
	if(length == 0) length = 1; // avoid div/0 errors
	for(int i = 0; i < 3; i++){
		normal[i] /= length;
	}
	glNormal3fv(normal);
	glVertex3fv(v1);
	glVertex3fv(v2);
	glVertex3fv(v3);
}
void drawConalCylinder(float x, float y, float z, float bottomRadius, float topRadius, float height){
	glPushMatrix();
	glTranslatef(x, y, z);
	gluCylinder(gluNewQuadric(), bottomRadius, topRadius, height, 25, 1);
	gluDisk(gluNewQuadric(), 0, bottomRadius, 25, 1);
	glTranslatef(0, 0, height);
	gluDisk(gluNewQuadric(), 0, topRadius, 25, 1);
	glPopMatrix();
}
void drawCylinder(float x, float y, float z, float radius, float height){
	drawConalCylinder(x, y, z, radius, radius, height);
}
void drawSegmentedSquare(){
	int slices = 10;
	glNormal3f(0, 0, 1);
	glTranslatef(-.5f, -.5f, 0);
	float d = 1.0f / slices;
	for(int i = 0; i < slices; i++){
		glBegin(GL_TRIANGLE_STRIP);
		for(int j = 0; j <= slices; j++){
			glVertex3f(d * j, d * (i + 1), 0); // ccw winding
			glVertex3f(d * j, d * i, 0);
		}
		glEnd();
	}
}
void drawCube(float x, float y, float z, float width, float height, float depth){
	glPushMatrix();
	glTranslatef(x, y, z);
	glScalef(width, height, depth);
	glPushMatrix();
	glTranslatef(0, 0, 1);
	drawSegmentedSquare();
	glPopMatrix();
	//glutSolidCube(1);
	glPopMatrix();
}

void placeLight(void){
	// todo
}
// rot: rotation around the z-axis
void drawStreetLight(float x, float y, float z, float rot){
	glPushMatrix();
	glTranslatef(x, y, z);
	glRotatef(rot, 0, 0, 1);
	//glColor3b(77, 40, 0);
	drawCylinder(0, 0, 0, streetLightThickness, streetLightHeight);
	glTranslatef(0, 0, streetLightHeight - streetLightThickness);
	glRotatef(90.0f - streetLightTheta, 0, 1, 0);
	for(int i = 0; i < 3; i++){
		drawCylinder(0, 0, 0, streetLightThickness, streetLightBranchLength);
		glTranslatef(0, 0, streetLightBranchLength);
		glRotatef(streetLightTheta, 0, 1, 0);
		//drawCylinder(0, 0, 0, streetLightThickness, streetLightBranchLength);
	}
	glRotatef(-1 * (streetLightTheta * 2 + 90), 0, 1, 0);
	drawConalCylinder(0, 0, -.25 * streetLightLampHeight, streetLightLampBottomRadius, streetLightLampTopRadius, streetLightLampTopRadius);
	placeLight();
	glPopMatrix();
}
// Draws a planecar in the [-11, 11][-15,15][0,8] space
void drawPlaneCar(void)
{
	glPushMatrix();
	glTranslatef(-11, -15, 0);
	float vectors[][3] = {
		{6, 0, 1},		// v0 
		{2, 0, 1},		// v1
		{6, 30, 1},		// v2
		{0, 10, 1},		// v3
		{0, 11, 2},		// v4
		{8, 11, 2},		// v5
		{10, 10, 1},	// v6
		{13, 11, 1},	// v7
		{15, 13, 3},	// v8
		{22, 14, 1},	// v9
		{22, 16, 1},	// v10
		{15, 17, 3},	// v11
		{0, 13, 3},		// v12
		{8, 13, 3},		// v13
		{10, 14, 4},	// v14
		{13, 14, 4},	// v15
		{13, 16, 4},	// v16
		{2, 30, 1},		// v17
		{10, 16, 4},	// v18
		{8, 17, 3},		// v19
		{0, 17, 3},		// v20
		{0, 19, 2},		// v21
		{8, 19, 2},		// v22
		{10, 20, 1},	// v23
		{13, 19, 1},	// v24
		{0, 20, 1},		// v25
		{2, 20, 8},		// v26
		{2, 19.5, 8},	// v27
		{2, 10.5, 8},	// v28
		{2, 10, 8},		// v29
		{5, 10, 8},		// v30
		{5, 10.5, 8},	// v31
		{5, 19.5, 8},	// v32
		{5, 20, 8},		// v33
		{0, 16.5, 3},	// v34
		{8, 16.5, 3},	// v35
		{8, 13.5, 3},	// v36
		{0, 13.5, 3},	// v37
		{10, 12, 0},	// v38
		{10, 18, 0},	// v39
		{1, 12, 0},		// v40
		{1, 18, 0},		// v41
		{12, 19, 1},	// v42
		{12, 11, 1},	// v43
		{0, 11, 1},		// v44
		{0, 19, 1}		// v45
	};
	int bodyQuads[][4] = {
		{1, 0, 5, 4},
		{4, 5, 8, 12},
		{12, 13, 19, 20},
		{20, 11, 22, 21},
		{21, 22, 2, 17},
		{8, 9, 10, 11},
		{25, 3, 4, 21},
		{21, 4, 12, 20},
		{23, 10, 9, 6},
		{3, 6, 0, 1},
		{3, 25, 23, 6},
		{17, 2, 23, 25},
		// bottom panel
		{39, 38, 40, 41}, // bottom
		{42, 43, 38, 39}, // front
		{44, 45, 41, 40}, // back
		{44, 40, 38, 43}, // right
		{45, 42, 39, 41}, // left
		// right tail fin
		{34, 27, 26, 20}, // back
		{19, 33, 32, 35}, // front
		{32, 33, 26, 27}, // top
		{34, 35, 32, 27}, // left
		{19, 20, 26, 33}, // right
		// left tail fin
		{36, 37, 28, 31}, // right
		{13, 36, 31, 30}, // front
		{37, 12, 29, 28}, // back
		{12, 13, 30, 29}, // left
		{30, 31, 28, 29} // top
	};
	int windowQuads[][4] = {
		{19, 13, 14, 18},
		{19, 18, 16, 11},
		{18, 14, 15, 16},
		{15, 8, 11, 16},
		{15, 14, 13, 8}
	};
	int triangles[][3] = {
		{1, 4, 3},
		{0, 6, 5},
		{6, 7, 5},
		{7, 8, 5},
		{7, 9, 8},
		{10, 24, 11},
		{24, 22, 11},
		{24, 23, 22},
		{23, 2, 22},
		{25, 21, 17}
	};

	glBegin(GL_TRIANGLES);
	float carColor[] = {.3, .1, .1, 0};
	glMaterialfv(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, carColor);
	int bodyQuadCount = sizeof(bodyQuads) / sizeof(bodyQuads[0]);
	for(int i = 0; i < bodyQuadCount; i++){
		drawTriangle(vectors[bodyQuads[i][0]], vectors[bodyQuads[i][1]], vectors[bodyQuads[i][3]]);
		drawTriangle(vectors[bodyQuads[i][3]], vectors[bodyQuads[i][1]], vectors[bodyQuads[i][2]]);
	}
	float windowColor[] = {.3, .3, .3, 0};
	float windowReflection[] = {1, 1, 1, 1};
	glMaterialfv(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, windowColor);
	glMaterialfv(GL_FRONT, GL_SPECULAR, windowReflection);
	int windowQuadCount = sizeof(windowQuads) / sizeof(windowQuads[0]);
	for(int i = 0; i < windowQuadCount; i++){
		drawTriangle(vectors[windowQuads[i][0]], vectors[windowQuads[i][1]], vectors[windowQuads[i][3]]);
		drawTriangle(vectors[windowQuads[i][3]], vectors[windowQuads[i][1]], vectors[windowQuads[i][2]]);
	}
	float resetSpecular[] = {0, 0, 0, 0};
	glMaterialfv(GL_FRONT, GL_SPECULAR, resetSpecular);
	glMaterialfv(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, carColor);
	int triangleCount = sizeof(triangles) / sizeof(triangles[0]);
	for(int i = 0; i < triangleCount; i++){
		drawTriangle(vectors[triangles[i][0]], vectors[triangles[i][1]], vectors[triangles[i][2]]);
	}
	glEnd();

	glPopMatrix();
}

void drawGroundAndRoad(void){
	// road lights
	float streetLightColor[] = {100.0f/256.0f, 100.0f/256.0f, 100.0f/256.0f, 1};
	glMaterialfv(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, streetLightColor);
	for(int y = -1; y <= 1; y += 2){
		float rotation = (y<0?90:-90);
		for(int x = (y < 0?-1*worldWidth/2+streetLightSpacing/2:-1 * worldWidth / 2); x < worldWidth / 2; x += streetLightSpacing){
			drawStreetLight(x, y * (roadWidth / 2 + streetLightMargin), 0, rotation);
		}
	}

	// grass
	//glColor3b(26, 51, 0);
	float grassColor[] = {26.0f/256.0f, 51.0f/256.0f, 0.0f/256.0f, 1};
	glMaterialfv(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, grassColor);
	drawCube(0, 0, -1.0f, worldWidth, worldWidth, 1);

	// road
	float mcolor[] = {41.0f/256.0f, 41.0f/256.0f, 41.0f/256.0f, 1};
	glMaterialfv(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, mcolor);
	//glColor3b(41, 41, 41);
	drawCube(0, 0, -0.5f, worldWidth, roadWidth, 1);
	// road solid stripe
	//glColor3f(1, 1, 1);
	float stripecolor[] = {1, 1, 1, 1};
	glMaterialfv(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, stripecolor);
	drawCube(0, roadWidth/2 - roadSolidStripeMargin, 0, worldWidth, roadStripeWidth, 1);
	drawCube(0, -1 * (roadWidth/2 - roadSolidStripeMargin), 0, worldWidth, roadStripeWidth, 1);
	// road middle stripe
	for(int x = -1 * worldWidth / 2; x < worldWidth / 2; x += roadStripeSpacing + roadStripeLength){
		drawCube(x, 0, 0, roadStripeLength, roadStripeWidth, 1);
	}
}

void display(void)
{
	frame++;
	//fprintf(stderr, "Frame: %x\n", frame);
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	glLoadIdentity();

	float mcolor[] = {.5, .5, .5, 1};
	glMaterialfv(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, mcolor);

	// Camera
	gluLookAt(carPos[0] + rotX, carPos[1] + rotY, rotZ + 500, carPos[0], carPos[1], 0, 0, 1, 0);

	drawGroundAndRoad();

	glPushMatrix();
	glTranslatef(carPos[0], carPos[1], carPos[2]);
	drawPlaneCar();
	glPopMatrix();	

	// double buffering
	glutSwapBuffers();

	carPos[0] += 5;
	carPos[2] = 0.25 * sin((float)frame);

	// queues a redisplay
	glutPostRedisplay();
}


/******************************************************************************
Initialize the OpenGL graphics context.
******************************************************************************/

void init(void) {

	/* select clearing color */ 

	glClearColor (0.0, 0.0, 0.0, 0.0);
	glShadeModel (GL_SMOOTH);
    glDepthFunc(GL_LESS);

	/* initialize viewing values */

	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	gluPerspective(90.0f, (GLfloat)width/(GLfloat)height, 1.0f, 6000.0f);
	glMatrixMode(GL_MODELVIEW);
	glEnable(GL_DEPTH_TEST);
	glEnable(GL_NORMALIZE);

	glShadeModel(GL_SMOOTH);
	glLoadIdentity();

	glEnable(GL_LIGHTING);
	GLfloat global_ambient[] = {.6f, .6f, .6f, 1.0f};
	glLightModelfv(GL_LIGHT_MODEL_AMBIENT, global_ambient);

	// Create light components
	GLfloat ambientLight[] = { .6, .6, .6, 1.0f };
	GLfloat diffuseLight[] = { .3, .3, .3, 1.0f };
	GLfloat specularLight[] = { .3, .3, .3, 1.0f };
	GLfloat position[] = { 0, 0, 50, 1.0f };
	GLfloat direction[] = { 0, 0, -1 };

	glEnable(GL_LIGHT0);
	// Assign created components to GL_LIGHT0
	glLightfv(GL_LIGHT0, GL_AMBIENT, ambientLight);
	glLightfv(GL_LIGHT0, GL_DIFFUSE, diffuseLight);
	glLightfv(GL_LIGHT0, GL_SPECULAR, specularLight);
	glLightfv(GL_LIGHT0, GL_POSITION, position);
	glLightfv(GL_LIGHT0, GL_SPOT_DIRECTION, direction);

	glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
}


/******************************************************************************
Process a keyboard action.  In particular, exit the program when an
"escape" is pressed in the window.
******************************************************************************/

void keyboard(unsigned char key, int x, int y) {
  /* set escape key to exit */
	fprintf(stderr, "%X\n", key);
	switch (key) {
	case 27: // escape key
		exit(0);
		break;
	case 0x3d:
	case 0x2b: // plus key
//		rot += 1;
		break;
	case 0x5f:
	case 0x2d: // minus key
//		rot -= 1;
		break;
	}

}

void specialKeyboard(int key, int x, int y){
	switch(key){
		case GLUT_KEY_UP:
			rotX += 2;
			break;
		case GLUT_KEY_DOWN:
			rotX -= 2;
			break;
		case GLUT_KEY_RIGHT:
			rotY -= 2;
			break;
		case GLUT_KEY_LEFT:
			rotY += 2;
			break;
		case GLUT_KEY_PAGE_DOWN:
			rotZ += 2;
			break;
		case GLUT_KEY_PAGE_UP:
			rotZ -= 2;
			break;
	}
}


/******************************************************************************
Main routine.
******************************************************************************/

int main(int argc, char** argv) {

	glutInit(&argc, argv);
	glutInitDisplayMode (GLUT_DOUBLE | GLUT_RGB | GLUT_DEPTH);
	glutInitWindowSize (width, height); 
	glutInitWindowPosition (0, 0);
	glutCreateWindow (argv[0]);
	init();

	glutDisplayFunc(display); 
	glutKeyboardFunc(keyboard);
	glutSpecialFunc(specialKeyboard);
	glutMainLoop(); 

	return 0;   /* ANSI C requires main to return int. */
}

