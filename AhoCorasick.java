import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class AhoCorasick {
	
	public static class Node {
		public char element; 
		public int output; 
		public List<Node> children;
		public Node failNode;
		public Node parent; 
		
		public Node(char element, int output) {
			this.element = element; 
			this.output = output; 
			this.children = new ArrayList<Node>();
		}
		
		@Override
		public boolean equals(Object n) {
			// TODO Auto-generated method stub
			if(n instanceof Node) {
				if(((Node) n).element == this.element) {
					return true;
				}
			}
			return false;
		}

		public int childExist(Node n) {
			return this.children.indexOf(n);
		}
		
		
		public void addChild(Node n) {
			children.add(n);
		}
		
		public void addOutput(int i) {
			this.output+=i;
		}
		
	}
	
	public static Node root = new AhoCorasick.Node('.',0);
	
	public static void buildTrie(String[] dictionary, int[] output, int first, int last) {
		for(int i =first; i<=last; i++) {
			String cWord = dictionary[i];
			Node currentRoot = AhoCorasick.root;
			for(int j=0; j<cWord.length(); j++) {
				char cChar = cWord.charAt(j);
				int o=0;
				if(j == cWord.length()-1) {
					o = output[i]; 
				}
				Node newNode = new Node(cChar,o);
				int index = currentRoot.childExist(newNode);
				if ( index == -1) {
					//no child
					System.out.println("Child : " +  newNode.element + " does not exist under current root "+currentRoot.element);
					System.out.println("Adding it and making it current root... ");
					currentRoot.addChild(newNode);
					newNode.parent = currentRoot;
					currentRoot = newNode; 
				}else {
					System.out.println("Child : " +  newNode.element + " does exist exist under current root "+currentRoot.element +", making it current root");
					currentRoot = currentRoot.children.get(index);
					if (newNode.output!=0) {
						System.out.println("Child got an output, adding it to existing node and then making it root");
						currentRoot.addOutput(newNode.output);
					}
				}
			}
		}
		
		//set up fail nodes
		//1. fail all 1st level to root
		
		Queue<Node> queue =new LinkedList<Node>();
		queue.add(root);
		while(!(queue.isEmpty())) {
			Node currentNode = queue.poll();
			if(currentNode.element == '.') {
				//1 fail all child to root
				for(Node n: currentNode.children) {
					n.failNode = currentNode; 
					queue.add(n);
				}
			}else {
				//go to fail of its parent and try to find itself 
				//if not found point to root; 
				for(Node n: currentNode.children) {
					Node failOfParent = n.parent.failNode;
					assert failOfParent != null; 
					int index = failOfParent.children.indexOf(n); 
					if( index != -1)
						n.failNode = failOfParent.children.get(index);
					else
						n.failNode = AhoCorasick.root;
					queue.add(n);
				}
			}
		}
		
		System.out.println("Done");
	}
	
	public static void main(String[] arg ) {
		
		String[] d = {"a", "ab", "bc","bbc","cd","a"};
		int[] o = {5,10,15,1,2,3};
		
		buildTrie(d,o,0,5);
		printChilds(root);
		
	}
	
	public static void printChilds(Node n) {
		System.out.println("Parent: "+n.element);
		System.out.println("Children :");
		for(Node no: n.children) {
			System.out.print(no.element+"("+no.output+")"+"Fail:"+no.failNode.element+"  ");
		}
		System.out.println();
		
		for(Node no: n.children)
			printChilds(no);
		
	}

}
