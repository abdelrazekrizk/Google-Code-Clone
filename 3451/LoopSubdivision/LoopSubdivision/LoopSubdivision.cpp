// LoopSubdivision.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
#include <gl/glut.h>
#include <vector>
#include "vertex.h"
#include "PlyReader.h"
#include "loop.h"

using namespace std;

#pragma warning( disable: 4018 ) // signed/unsigned comparison mismatch

#define WINWIDTH 600.0
#define WINHEIGHT 600.0

//////////////////////////////////////
// 'constants'
const float color[] = { .6f, .6f, .6f, 1.0f };
const float zero[] = { 0.0f, 0.0f, 0.0f, 0.0f };

//////////////////////////////////////
// Runtime variables
int frame = 0;
model* baseModel;
vector<vertex*>* baseFaceNormals;
model* loopModel;
vector<vertex*>* loopVertexNormals;

//////////////////////////////////////
// Functions

void drawNormals(model* m, vector<vertex*>* faceNormals){
	glBegin(GL_LINES);
	//glColor3d(.3, .3, .8);
	for(int i = 0; i < m->faces.size(); i++){
		vector<int> face = m->faces[i];
		vertex centroid(0, 0, 0);
		centroid.x = (m->vertices[face[0]]->x + m->vertices[face[1]]->x + m->vertices[face[2]]->x) / 3.0;
		centroid.y = (m->vertices[face[0]]->y + m->vertices[face[1]]->y + m->vertices[face[2]]->y) / 3.0;
		centroid.z = (m->vertices[face[0]]->z + m->vertices[face[1]]->z + m->vertices[face[2]]->z) / 3.0;
		vertex normal = centroid + *faceNormals->at(i);
		glVertex3fv((float*)&centroid);
		glVertex3fv((float*)&normal);
	}
	glEnd();
}

void drawModelShadedByFace(model* m, vector<vertex*>* faceNormals){
	glShadeModel(GL_FLAT);
	glPolygonMode(GL_FRONT, GL_FILL);
	glBegin(GL_TRIANGLES);
	for(int i = 0; i < m->faces.size(); i++){
		vector<int> face = m->faces[i];
		vertex *norm = faceNormals->at(i);
		glNormal3fv((float*)norm);
		glVertex3fv((float*)m->vertices[face[0]]);
		glVertex3fv((float*)m->vertices[face[1]]);
		glVertex3fv((float*)m->vertices[face[2]]);
	}
	glEnd();
	//drawNormals(m, faceNormals);
}

void drawModelShadedByVertex(model* m, vector<vertex*>* vertexNormals){
	glShadeModel(GL_SMOOTH);
	glPolygonMode(GL_FRONT, GL_FILL);
	glBegin(GL_TRIANGLES);
	for(int i = 0; i < m->faces.size(); i++){
		vector<int> face = m->faces[i];
		glNormal3fv((float*)vertexNormals->at(face[0]));
		glVertex3fv((float*)m->vertices[face[0]]);
		glNormal3fv((float*)vertexNormals->at(face[1]));
		glVertex3fv((float*)m->vertices[face[1]]);
		glNormal3fv((float*)vertexNormals->at(face[2]));
		glVertex3fv((float*)m->vertices[face[2]]);
	}
	glEnd();
}

// display loop
void display(void){
	frame++;
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	glLoadIdentity();
	glMaterialfv(GL_FRONT, GL_AMBIENT_AND_DIFFUSE, color);
	glMaterialfv(GL_FRONT, GL_SPECULAR, zero);

	gluLookAt(2, 2, 2, 0, 0, 0, 0, 0, 1);
	glRotated(frame * .1, 0, 0, 1);
	//if(frame < 1080 * 2) { // for 3 rotations show original model
	if(frame < 360 * 10){
		drawModelShadedByFace(baseModel, baseFaceNormals);
	} else {
		drawModelShadedByVertex(loopModel, loopVertexNormals);
	}

	// swap buffers, queue redisplay
	glutSwapBuffers();
	glutPostRedisplay();
}

void printUsageAndExit(void){
	fprintf(stdout, "Project5.exe [model] [subdivide#]\n");
	fprintf(stdout, "e.g. Project5.exe octa.ply 5\n");
	exit(0);
}

vector<vertex*>* calculateFaceNormals(model* m){
	vector<vertex*>* normals = new vector<vertex*>;
	for each (vector<int> face in m->faces){
		vertex a = *(m->vertices[face[0]]);
		vertex b = *(m->vertices[face[1]]);
		vertex c = *(m->vertices[face[2]]);
		vertex f = b - a;
		vertex g = c - a;
		normals->push_back(new vertex(f.crossp(g)));
		//normals->push_back(g.crossp(f));
	}
	return normals;
}

bool faceContains(vector<int>* face, int vertexid){
	for(int i = 0; i < face->size(); i++){
		if(face->at(i) == vertexid) return true;
	}
	return false;
}

vector<vertex*>* calculateVertexNormals(model* m){
	vector<vertex*>* normals = new vector<vertex*>;
	vector<vertex*>* faceNormals = calculateFaceNormals(m);
	fprintf(stdout, "%i vertices to calculate normals for.\n", m->vertices.size());
	for (int i = 0; i < m->vertices.size(); i++){
		int count = 0;
		vertex sum;
		for(int j = 0; j < m->faces.size(); j++){
			if(faceContains(&m->faces[j], i)){
				count++;
				sum = sum + *faceNormals->at(j);
			}
		}
		normals->push_back(new vertex(sum / (float)count));
		if(((m->vertices.size() - i) % 500) == 0){
			fprintf(stdout, "%i vertices left...\n", m->vertices.size() - i);
		}
	}
	fprintf(stdout, "Vertex normals calculations completed!\n");
	return normals;
}

void init(string filename, int subdivisions){
	fprintf(stdout, "Reading in model from %s...\n", filename.c_str());
	baseModel = readModel(filename);
	fprintf(stdout, "Calculating (face) normals for base model...\n");
	baseFaceNormals = calculateFaceNormals(baseModel);
	fprintf(stdout, "Performing loop subdivision on model %i times...\n", subdivisions);
	loopModel = loop(baseModel, subdivisions);
	fprintf(stdout, "Calculating (vertex) normals for subdivided model...\n");
	loopVertexNormals = calculateVertexNormals(loopModel);

	glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

	glDepthFunc(GL_LESS);
	
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	gluPerspective(90.0f, WINWIDTH / WINHEIGHT, 0.01f, 50.0f);

	glMatrixMode(GL_MODELVIEW);
	glEnable(GL_DEPTH_TEST);
	glEnable(GL_NORMALIZE);

	glEnable(GL_LIGHTING);
	glEnable(GL_LIGHT0);

	glLoadIdentity();

	float ambient[] = {0, 0, 0, 1};
	float diffuse[] = {1, 1, 1, 1};
	float specular[] = {1, 1, 1, 1};
	float pos[] = {10, 10, 0};
	glLightfv(GL_LIGHT0, GL_AMBIENT, ambient);
	glLightfv(GL_LIGHT0, GL_DIFFUSE, diffuse);
	glLightfv(GL_LIGHT0, GL_SPECULAR, specular);
	glLightfv(GL_LIGHT0, GL_POSITION, pos);

	glPolygonMode(GL_FRONT, GL_FILL);
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

int main(int argc, char** argv)
{
	// argument fixing for testing
	argv = new char*[3];
	argv[0] = "LoopSubdivision.exe";
	argv[1] = "star.ply";
	argv[2] = "3";
	argc = 3;

	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_DOUBLE | GLUT_RGB | GLUT_DEPTH);
	glutInitWindowSize(WINWIDTH, WINHEIGHT);
	glutInitWindowPosition(0, 0);

	glutCreateWindow("Loop Subdivision");

	if(argc == 3){
		string filename = argv[1];
		int subdivisions = atoi(argv[2]);
		init(filename, subdivisions);
	} else {
		fprintf(stdout, "Incorrect number of arguments (expected 2). Usage:\n");
		printUsageAndExit();
	}

	glutDisplayFunc(display);
	glutKeyboardFunc(keyboard);
	glutSpecialFunc(specialKeyboard);
	glutMainLoop();

	return 0;
}

