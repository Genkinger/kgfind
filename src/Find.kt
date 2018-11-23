import java.io.File;
import kotlin.math.*;

data class Vector2(var x: Double, var y: Double)
{
	operator fun plus(vec: Vector2) = Vector2(vec.x + x, vec.y + y)
	operator fun minus(vec: Vector2) = Vector2(x - vec.x, y - vec.y)
	operator fun times(scalar: Double) = Vector2(x*scalar,y*scalar)
	fun length(): Double = sqrt(x*x + y*y)
}

data class Node(
     val name_internal: String,
     val name_display: String,
     val position: Vector2,
     val neighbours: List<String>,
     var distance: Double = Double.POSITIVE_INFINITY,
     var lastNode: Node? = null,
     var done: Boolean = false){}
     
data class Graph(val nodes: List<Node>){
     fun GetShortestPath(start: String, end: String): List<Node> {
     	 val endNode = GetNode(end)
	 var currentNode = GetNode(start)
	 currentNode!!.distance = 0.0

	 while(!nodes.filter{it.neighbours.size != 0 && it.done == false}.isEmpty() && currentNode != null)
	 {
		currentNode.done = true
	  	currentNode.neighbours.forEach{
			val node = GetNode(it)
			if(!node!!.done){
		
				val dist = currentNode.distance + (node.position - currentNode.position).length();
				if(node.distance > dist)
				{
					node.distance = dist
					node.lastNode = currentNode
				}
			}
	 	}
		currentNode = GetNode(currentNode.neighbours.filter{GetNode(it).done == false}.minBy{GetNode(it).distance}!!)
		println("Next Node: $currentNode")
	}

	 val path = mutableListOf<Node>()
	 if(endNode!!.lastNode != null)
	 {
		var currentNode = endNode.lastNode
		while(currentNode!!.lastNode != null)
		{
			path.add(currentNode)
			currentNode = currentNode.lastNode!!
		}
	 }
	 
		path.reverse()
	 	return path
     }

     fun GetNode(name: String): Node?{
     	 println("Getting Node: $name...")
     	 return nodes.find{it.name_internal == name}
     }
}


fun loadNodes(dataPath: String): List<Node>
{
	val contents = File(dataPath).readText()
	val nodes = mutableListOf<Node>()
	
	contents
	.lines()
	.forEach{line ->
		val parts = line.split(';')
		val name_internal = parts[0]
		val name_display = parts[1]
		val x = parts[2].replace(',','.').toDouble()
		val y = parts[3].replace(',','.').toDouble()
		val neighbours = parts[5].replace('.',',').split(',').filter{it != "E2" && it != "E1"}		
		nodes.add(Node(name_internal,name_display,Vector2(x,y),neighbours))
	}
	return nodes
}

fun main(args: Array<String>)
{
	val nodes = loadNodes(args[0])
	val graph = Graph(nodes)
	
	println(graph.GetShortestPath("E3","E9"))
}

