# MyLightsServer
### Server for MyLights Project

##### ER Diagram
![DB Architecture](db/architektur.png)

##### API
```
GET
/
/h2-console


POST
/group/create
    -   groupName
/group/update
    -   groupId
    -   groupName
/group/delete
    -   groupId
/group/get/all

/command/set
    -   jsonData
/command/get/all
/command/get/group
    -   groupId

/stripe/register
    -   ip
    -   mac
/stripe/unassign
    -   stripeId
/strupe/update
    -   stripeId
    -   stripeName
/stripe/get/all
/stripe/set/group
    -   stripeId
    -   groupId
/stripe/get/group
    -   groupId
/stripe/get/unassigned
```


##### Example JSON Command
````json
{
	"command":
	{
		"mode":"m",
		"secondsToNextColor":2,
		"groupId":1,
		"colors":
		[
			{
				"red":255,
				"green":0,
				"blue":0
			},
			{
				"red":0,
				"green":255,
				"blue":0
			},
			{
				"red":0,
				"green":0,
				"blue":255
			}
		]
	}
}
````