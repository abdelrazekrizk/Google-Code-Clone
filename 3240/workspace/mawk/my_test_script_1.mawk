BEGIN{print("--begin output--")}

for(3) {
	a		{print("I'm in a for loop with a")}
}
while(a*) {
	a*		{insert(1,a)}
	aaaa	{remove(a); insert(1,b)}
	a*		{print(LINE)}
}
a		 	{print("Test 1 success")}
b		 	{print("Test 2 success")}
c		 	{print("Test 3 success")}

END{print("--end output--")}