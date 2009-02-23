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

// Draws a planecar in the [-11, 11][-15,15][-4,4] space
void drawplanecar(void)
{
	glPushMatrix();
	glTranslatef(-11, -15, -4);
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
	int quads[][4] = {
		{1, 0, 5, 4},
		{4, 5, 8, 12},
		{12, 13, 19, 20},
		{20, 11, 22, 21},
		{21, 22, 2, 17},
		{19, 13, 14, 18},
		{19, 18, 16, 11},
		{18, 14, 15, 16},
		{15, 8, 11, 16},
		{15, 14, 13, 8},
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

	srand((unsigned)time(NULL));

	glBegin(GL_QUADS);
	glColor3f(.25, .25, 1);
	int quadCount = sizeof(quads) / sizeof(quads[0]);
	for(int i = 0; i < quadCount; i++){
		glColor3f((float)rand()/RAND_MAX, (float)rand()/RAND_MAX, (float)rand()/RAND_MAX);
		for(int j = 0; j < 4; j++){
			glVertex3fv(vectors[quads[i][j]]);
		}
	}
	glEnd();

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
	int triangleCount = sizeof(triangles) / sizeof(triangles[0]);
	for(int i = 0; i < triangleCount; i++){
		glColor3f((float)rand()/RAND_MAX, (float)rand()/RAND_MAX, (float)rand()/RAND_MAX);
		for(int j = 0; j < 3; j++){
			glVertex3fv(vectors[triangles[i][j]]);
		}
	}
	glEnd();
	
	glPopMatrix();
}

void drawRoad(void){
	glPushMatrix();
	glBegin(GL_QUADS);
	glColor3f(.5, .5, .5);
	glVertex3f(-300, -300, 0);
	glVertex3f(300, -300, 0);
	glVertex3f(300, 300, 0);
	glVertex3f(-300, 300, 0);
	glEnd();
	glPopMatrix();
}

void display(void)
{
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	glPushMatrix();
	glTranslatef(0, 0, -27);
	//glPolygonMode(GL_FRONT, GL_FILL);
	drawRoad();
	glTranslatef(0, 0, 4);
	glPushMatrix();
	glRotatef(rotX, 1, 0, 0);
	glRotatef(rotY, 0, 1, 0);
	glRotatef(rotZ, 0, 0, 1);
	drawplanecar();
	glutSolidCube(3);
	glPopMatrix();
	glPopMatrix();

	glutSwapBuffers();
	//rot += 2;
	glutPostRedisplay();
}


/******************************************************************************
Initialize the OpenGL graphics context.
******************************************************************************/

void init(void) {

  /* select clearing color */ 

  glClearColor (0.0, 0.0, 0.0, 0.0);
  glShadeModel (GL_SMOOTH);

  /* initialize viewing values */

  glMatrixMode(GL_PROJECTION);
  glLoadIdentity();
  gluPerspective(90.0f, (GLfloat)width/(GLfloat)height, 1.0f, 100.0f);
  glMatrixMode(GL_MODELVIEW);
  glEnable(GL_DEPTH_TEST);
  glLoadIdentity();

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

