package Jaxaar.BARSOverlay.Utils

object Helpers {


	//	Improve**
//	Removes all minecraft string formatting from the string
	def stripColorCodes(str: String): String = {
		var strOut = str
		var index = 0
		while(index < strOut.length){
			if(strOut(index) == '§'){
				strOut = strOut.substring(0, index) + strOut.substring(index+2, strOut.length)
			}
			index += 1
		}
		strOut
	}
}
