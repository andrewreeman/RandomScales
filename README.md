# RandomScales
android android-studio

Application for choosing a random scale or arpeggio to play. Ability to select randomly from preset list and to save presets.



*Ideas*

**Serialized data structures**

***Keys, Scales and Styles ***
We will store a list of keys in the file 'Entities/keys.json'
We will store a list of scales in the file 'Entities/scales.json'
We will store a list of styles in the file 'Etntities/styles.json'

Each 'entity' will be stored similar to the following structure, this example will be keys.json:

{
	"name": "Keys",
	"entities": [  // a list of entities with an id and a name
		{"id": 0, "name": "C"}, 
		{"id": 1, "name": "C#"},
		...
		{"id": 11, "name": "B"}

	]
}

***Selectable exercises***
We would like the specify what keys, scales and styles we would like the random selection to be taken from. 
We would like to think of this as essentially a node graph where each key has a list of scales it could be and each scale has a list of styles it could be. 

key: [
	scale: [
		styles: []
	],
	scale: [
		styles: []
	]
],
key: [
	...
]

There is room for a lot of redundency in this structure though as most scales in each key will be the same selection as will be the styles. 
We would like to state the base 'pool' that the entities will be and then a 'diff graph' that states if any key has different scales from the pool...

{

// all the keys stated here will have all the scales stated here which will have all the styles stated here
"baseKeyIds": [0, 5, 8] ,
"baseScaleIds": [5, 1, 13],
"baseStyleIds": [7,1,3],

"diffGraph": [
	{ "key": 0,
	"diffType": "+" // diff type '+' means that this key will have all the baseScales plus this given scale. If this scale already is included we will still do the same for the style. 
	"scales": 
		[
			{
				"scale": 3,
				"diffType": "-" // diff type '-' means that this scale will have the base styles except the styles listed here. If the style is not in the base list then nothing happens
				"styles": [7, 13]			
			},
			{
				"scale": 2,
				"diffType": "*"	, // diff type '*' means that the styles for this scale will the base styles. The styles array is ignored if present		
			},
			{
				"scale": 4,
				"diffType": "_" // diff type "_" means that the base styles are ignored and we will just pick from this given list instead
				"styles": [8, 4, 2]
			}
			
		]	
	},
	{
		"key": 0,
		"diffType": "-" // As described above. This type means that we will use the base presets but will be removing scale '13' for this key. Note: this is the same key as before. 
		"scales": [13]		
	},
	{
		"key": 0,
		"diffType": "*"
		"styles": [3, 5, 4] // for ALL scales in this key just use the given styles in the array...OR....
		OR
		"styles": [ // for ALL scales in this key add style 2 to the base set and remove style 1 from the base set
			{
				"style": 2,
				"diffType": "+"
			},
			{
				"style": 1,
				"diffType": "-"
			}
		]
	}

]
}





