package Jaxaar.BARSOverlay.Utils

import java.util.function.{BiConsumer, Consumer}
import scala.collection.mutable

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




	def MapAsScala [A, B](inMap: java.util.HashMap[A, B]): Map[A, B]= {
		val outMap = mutable.Map[A,B]()
		val func = new BiConsumer[A, B]() {
			def accept(a: A, b: B ): Unit = {
				outMap.put(a, b)
			}
		}
		inMap.forEach(func)
		outMap.toMap
	}

	def ListAsScala [A](inLst: java.util.List[A]): List[A]= {
		(for (i <- 0 until inLst.size()) yield inLst.get(i)).toList
	}

	def CollectionAsScala [A](inLst: java.util.Collection[A]): List[A]= {
		val outLst = mutable.ListBuffer[A]()
		val func = new Consumer[A]() {
			def accept(a: A) = {
				outLst.append(a)
			}
		}
		inLst.forEach(func)
		outLst.toList
	}

	def ListAsJava [A](inLst: List[A]): java.util.List[A]= {
		val outLst = new java.util.ArrayList[A]()
		inLst.foreach(x => outLst.add(x))
		outLst
	}
}
