package dlgre.formula;

import util.StringUtils.join;


case class Conjunction(sub:List[Formula]) extends Formula {
  	override def isSatisfied(u:String, graph:Graph[String]) = {
          sub.forall { f => f.isSatisfied(u,graph) }  
        }
          
          
	override def prettyprint = {
          join(sub.map { f => f.prettyprint }, " & ");
        }
        
        override def flatten = {
          val results = for( x <- sub if ! x.isInstanceOf[Top] ) yield {
            val f = x.flatten;
            
            f match {
              case Conjunction(l) => l
              case _ => List(f)
            }
          }
          
          conjoin(results.flatten { x => x })
        }
}