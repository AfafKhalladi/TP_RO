# Recherche Operationnelle - M1 BDMA
## Subject
During classes, you have studied two typical Operation Research problems, the Knapsack Problem (KP) and the Travelling Salesman Problem (TSP).
We will now study a problem that takes aspects from both KP and TSP. This problem stems from the need to automate Exploratory Data Analysis (EDA), 
typically as BDMA students given an unknown database you would spend several hours running queries to extract interesting information for a business case.
Our goal is to automate part of this process by showing you a sequence of interesting queries. 
This can be seen as an optimization problem. For a set of possible queries on a database we create a complete graph, on each node the cost to run the query and its interestingness. On each edge the distance between the two queries.
We then look for a path in the graph (A sequence of nodes) with the maximum interestingness given a cost and distance limit.
This problem is called te Travelling Analyst Problem (TAP) and is closely related to transport problems such as the Orienteering Problem.
A detailed explanation is available [here](https://hal.archives-ouvertes.fr/hal-03217380/file/tap_roadef_final.pdf).

The baseline strategy you should beat is simply sorting queries by interestingness and building the sequence until one constraint is violated.
## Expected work
This is a 2-person 'project' that will encompass the 3 lab sessions dedicated to OR. Your goal is to propose one or more heuristic(s) to solve the TAP problem and implement them following the TAPSolver Interface.
You will document your work and reasoning behind your algorithm(s) in a lab report. 
### Grading
Each 2-person team will be graded on a 2 or 3 pages (hard limit) report, the code quality, and the performance of the heuristics (runtime, objective).
Your heuristics will be tested on other instances as well as the 5 provided in this repository. 

## Exemple (Naive Solution)
Create a class `TestNaif` that implements the `TAPSolver` interface. 
As you can see in the interface we need to implement a solve method with the following signature `public List<Integer> solve(Instance ist)`. 
To construct the solution we will simply chain the queries in the order they appear in the `Instance` until we violate one epsilon constraint.

To do so we can use the `Objectives` object:
```java
        List<Integer> demo = new ArrayList<>();
        Objectives obj = new Objectives(ist);
        
        int q_idx = 0;
        
        while (obj.distance(demo) < ist.getMaxDistance() && obj.time(demo) < ist.getTimeBudget()){
            demo.add(q_idx++);
        }
        return demo.subList(0, demo.size() - 1);
```

## Using your own PC
You should have Maven installed and an updated JDK.

Download Project
```shell
git clone https://github.com/AlexChanson/RechercheOperationnelle-M1-BDMA
```
Move to project and test build
```shell
cd .\RechercheOperationnelle-M1-BDMA\
mvn package
```

### Run your code 
```shell
java -cp target/TP-RO-M1-BDMA-1.0-SNAPSHOT.jar com.alexscode.teaching.Main
```

## Using computer lab PCs
### Ouvrir powershell
Set proxy for git
```
git config --global https.proxy http://proxy:3128
git config --global http.proxy http://proxy:3128
```

Download Project
```shell
git clone https://github.com/AlexChanson/RechercheOperationnelle-M1-BDMA
```

Setup maven (Only use last line if you have already downloaded)
```
wget https://dlcdn.apache.org/maven/maven-3/3.8.8/binaries/apache-maven-3.8.8-bin.zip -UseBasicParsing -O maven.zip
Expand-Archive .\maven.zip .
$env:Path += 'U:\apache-maven-3.8.8\bin'
```

Set Proxy for maven
```shell
mkdir $Env:userprofile\.m2
cp .\RechercheOperationnelle-M1-BDMA\config\settings.xml $Env:userprofile\.m2\settings.xml
```


Check if it's working with `mvn --version`

Setup Java Environment
```
$Env:JAVA_HOME = "C:\Program Files\Java\jdk1.8.0_341\"
```

Move to project and test build
```shell
cd .\RechercheOperationnelle-M1-BDMA\
mvn package
```

### Run your code
```shell
java -cp target/TP-RO-M1-BDMA-1.0-SNAPSHOT.jar com.alexscode.teaching.Main
```



