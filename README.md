# MyLightsServer
Server for MyLights Project

ER Diagram
![DB Architecture](db/architektur.png)

Example JSON Command
````json
{
	"command":
	{
		"mode":"m",
		"secondsToNextColor":12,
		"groupId":1,
		"colors":
		[
			{
				"red":255,
				"blue":0,
				"green":0
			},
			{
				"red":0,
				"blue":255,
				"green":0
			},
			{
				"red":0,
				"blue":0,
				"green":255
			}
		]
	}
}
````