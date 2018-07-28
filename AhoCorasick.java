import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

public class AhoCorasick {
	
	public static class Node {
		public char element; 
		public List<Integer> output; 
		public Node[] children;
		public Node failNode;
		public Node parent;
		
		public Node(char element, int o, int maxOutput) {
			this.element = element; 
			this.output = new ArrayList<Integer>();
			//Arrays.fill(this.output, -1);
			if(o != -1) {
				this.output.add(o);
			}
				
			this.children = new Node[26];
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

		public Node childExist(Node n) {
			return this.children[n.element - 'a'];
		}
		
		
		public boolean addChild(Node n) {
			
			if(children[n.element - 'a'] == null) {
				children[n.element - 'a'] = n;
				n.parent = this;
				return true;
			}
			else 
				children[n.element - 'a'].addOutput(n.output.get(0));
				return false;
		}
		
		public boolean addChild(char element, int o, int maxOutputLength) {
			
			if(children[element - 'a'] == null) {
				children[element - 'a'] = new Node(element, o, maxOutputLength);
				children[element - 'a'].parent = this;
				return true;
			}
			else 
				children[element - 'a'].addOutput(o);
				return false;
		}
		
		public void addOutput(int i) {
			if(i!=-1) {
				this.output.add(i);
			}
				
		}
		
	}
	
	public static final Node root = new AhoCorasick.Node('.',-1,1);
	
	public static void buildTrie(String[] dictionary, int[] output) {
		root.failNode = root;
		for(int i =0; i<dictionary.length; i++) {
			//System.err.println("Processing : "+i);
			String cWord = dictionary[i];
			Node currentRoot = AhoCorasick.root;
			for(int j=0; j<cWord.length(); j++) {
				char cChar = cWord.charAt(j);
				int o=-1;
				if(j == cWord.length()-1) {
					o = i; 
				}
				//Node newNode = new Node(cChar,o, output.length);
				currentRoot.addChild(cChar,o, output.length);
				currentRoot = currentRoot.children[cChar - 'a'];
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
					if(n == null) continue;
					n.failNode = currentNode; 
					queue.add(n);
				}
			}else {
				//go to fail of its parent and try to find itself 
				//if not found point to root; 
				for(Node n: currentNode.children) {
					if(n==null) continue;
					Node failOfParent = n.parent.failNode;
					if( failOfParent == null) {
						System.err.println("PARENT OF "+n.parent.element + " IS NULL");
						break;
					}
					Node searchNode = failOfParent.children[n.element-'a']; 
					if( searchNode != null)
						n.failNode = searchNode;
					else
						n.failNode = AhoCorasick.root;
					queue.add(n);
				}
			}
		}
		
		//System.out.println("Done");
	}
	
	public static int process(String dna, int[] health,  int first, int last) {
		//System.out.println("Processing dna :"+dna);
		Node pointer=root;
		Node index;
		int ho =0;
		for(int i =0; i<dna.length();) {
			char curChar = dna.charAt(i);
			//System.out.println("Current char :" +curChar);
			index = pointer.childExist(new Node(curChar, -1, health.length));
			if(index != null) {
				//System.out.println("Found "+curChar +" as child of "+ pointer.element);
				pointer = index;
				i++;
			}else {
				if(pointer.equals(root)) {
					//System.out.println("Failing on root "+pointer.element + "For "+ curChar);
					i++;
				}else {
					//System.out.println("Failing on : "+pointer.element + "For "+ curChar);
				}
				pointer = pointer.failNode;
				if(pointer == null ) {
					System.err.println("WARNING");
				}
				
			}
			
			//System.out.println("out size for : "+pointer.element + " "+ pointer.output.size());
			if(pointer.output.size()>0) {
				for(int ind: pointer.output) {
					//System.out.println(ind);
					if(ind >= first && ind <= last) {
						//System.out.println(health[ind]+ "+ ");
						ho+=health[ind];
					}
				}
			}
			
		}
		
		return ho;
	}
	
	private static final Scanner scanner = new Scanner(System.in);
	
	public static void test() {
		
		String[] genes = {"a" ,"b" ,"c" ,"aa" ,"d" ,"b"};
		int[] health = {1, 2 ,3, 4, 5 ,6};
		
		buildTrie(genes, health);
		printChilds(root);
		System.out.println(process("caaab", health, 1, 5));
		System.out.println(process("xyz", health, 0, 4));
		System.out.println(process("bcdybc", health, 2, 4));
		
		
	}

    public static void main(String[] args) {
    	test();
        int n = scanner.nextInt();
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

        String[] genes = new String[n];

        String[] genesItems = scanner.nextLine().split(" ");
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

        for (int i = 0; i < n; i++) {
            String genesItem = genesItems[i];
            genes[i] = genesItem;
        }

        int[] health = new int[n];

        String[] healthItems = scanner.nextLine().split(" ");
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

        for (int i = 0; i < n; i++) {
            int healthItem = Integer.parseInt(healthItems[i]);
            health[i] = healthItem;
        }

        int s = scanner.nextInt();
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        
        buildTrie(genes, health);
        
        
        
        for (int sItr = 0; sItr < s; sItr++) {
        	
            String[] firstLastd = scanner.nextLine().split(" ");

            int first = Integer.parseInt(firstLastd[0]);

            int last = Integer.parseInt(firstLastd[1]);

            String d = firstLastd[2];
            
            int h = process(d, health, first, last);
            //System.out.println(h);
            min = h<min?h:min; 
            max = h>max?h:max;
            //System.out.println("Exit");
        }
        
        System.out.println(min+" "+max);
        scanner.close();
    }
	
	public static void printChilds(Node n) {
		System.out.println("Parent: "+n.element);
		System.out.println("Children :");
		for(Node no: n.children) {
			if(no == null) continue;
			System.out.print(no.element+"Fail:"+no.failNode.element+"  ");
			System.out.print( "Output Indexes (");
			for(int i : no.output)
			  System.out.print(i+" ");
			System.out.println(" )");
			System.out.println();
		}
		System.out.println();
		
		for(Node no: n.children) {
			if(no ==null) break;
			printChilds(no);
		}
			
		
	}

}
