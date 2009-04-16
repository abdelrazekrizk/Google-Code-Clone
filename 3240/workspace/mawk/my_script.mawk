BEGIN{print("--begin output--")}

a(a|b|c)* 	{substring(1, EOL); insert(EOL, a)}
b(a|b|c)* 	{replace(a,b)}
c(a|b|c)* 	{replace(a,c)}
			{print(LINE)}

END{print("--end output--")}