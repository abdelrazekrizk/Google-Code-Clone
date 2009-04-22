// Project5.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
#include <glut.h>
//#include <fstream>

#define WWIDTH 600
#define WHEIGHT 600

// prototypes
void printUsage(void);

// Display function
void display(void){
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	glLoadIdentity();

	// do stuff here

	// swap buffers, queue another display
	glutSwapBuffers();
	glutPostRedisplay();
}

// loads model from file if the syntax is correct. If not correct, then prints usage and exits.
void myLoadModel(int argc, char** argv)
{
	if(argc == 2){


	} else { // incorrect number of args
		fprintf(stdout, "Incorrect number of arguments (expected 2). Usage:\n");
		printUsage();
		exit(0);
	}
}

// Initializing function
void init(int argc, char** argv)
{
	myLoadModel(argc, argv);
	glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
	glShadeModel(GL_SMOOTH);
	glDepthFunc(GL_LESS);

	// view projection
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	gluPerspective(90.0f, WWIDTH / WHEIGHT, 0.01f, 50.0f);

	glMatrixMode(GL_MODELVIEW);
	glEnable(GL_DEPTH_TEST);
	glEnable(GL_NORMALIZE);
	glLoadIdentity();

	glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
}

// normal keyboard functions
void keyboard(unsigned char key, int x, int y)
{
	switch(key){
		case 27: // exit key
			exit(0);
			break;
	}
}

// special keyboard functions
void specialKeyboard(int key, int x, int y)
{
	// use special keys IE GLUT_KEY_UP
}

// Program
int main(int argc, char** argv)
{
	// glutinit modifies argc and argv, extracting any args intended for glut.
	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_DOUBLE | GLUT_RGB | GLUT_DEPTH);
	glutInitWindowSize(WWIDTH, WHEIGHT);
	glutInitWindowPosition(0, 0);
	//glutCreateWindow(argv[0]);
	glutCreateWindow("Project 5");
	init(argc, argv);

	glutDisplayFunc(display);
	glutKeyboardFunc(keyboard);
	glutSpecialFunc(specialKeyboard);
	glutMainLoop();

	return 0; // ansi c required
}

void printUsage(void){
	fprintf(stdout, "Project5.exe [model] [subdivide#]\n");
	fprintf(stdout, "e.g. Project5.exe octa.ply 5");
}