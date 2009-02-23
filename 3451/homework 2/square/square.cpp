/*

A sample OpenGL animation of a rotating square.

Greg Turk, April 1998

*/

#include "stdafx.h"
#include <math.h>
#include <stdio.h>
#include <stdlib.h>
#include <glut.h>

static GLfloat spin = 0.0;


/******************************************************************************
Display a rotating square.
******************************************************************************/

void display(void)
{
	int i;

  for (i = 0; i < 200; i++) {
	spin = spin + 2.0;

	glClear(GL_COLOR_BUFFER_BIT);
	glPushMatrix();
	glRotatef(spin, 0, 0, 1.0);
	glColor3f(1.0, 1.0, 1.0);
	glRectf(-25.0, -25.0, 25.0, 25.0);
	glPopMatrix();

	glutSwapBuffers();
  }
}


/******************************************************************************
Initialize the OpenGL graphics context.
******************************************************************************/

void init(void) {

  /* select clearing color */ 

  glClearColor (0.0, 0.0, 0.0, 0.0);
  glShadeModel (GL_FLAT);

  /* initialize viewing values */

  glMatrixMode(GL_PROJECTION);
  glLoadIdentity();
  glOrtho(-50.0, 50.0, -50.0, 50.0, -1.0, 1.0);

}


/******************************************************************************
Process a keyboard action.  In particular, exit the program when an
"escape" is pressed in the window.
******************************************************************************/

void keyboard(unsigned char key, int x, int y) {
  fprintf(stderr, "%x\n", key);
  /* set escape key to exit */

  switch (key) {
    case 27:
      exit(0);
      break;
  }

}


/******************************************************************************
Main routine.
******************************************************************************/

int main(int argc, char** argv) {

  glutInit(&argc, argv);
  glutInitDisplayMode (GLUT_DOUBLE | GLUT_RGB);
  glutInitWindowSize (250, 250); 
  glutInitWindowPosition (100, 100);
  glutCreateWindow (argv[0]);
  init ();

  glutDisplayFunc(display); 
  glutKeyboardFunc(keyboard);
  while(1){
	glutMainLoop(); 
  }
  return 0;   /* ANSI C requires main to return int. */
}

