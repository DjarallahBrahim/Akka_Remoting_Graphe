compile : mvn package

Normal test single machine Tree :run : java -jar app-1.0-allinone.jar <systemName> <file_json>(tree.json) <port>


Normal test single machine Graphe :run : java -jar app-1.0-allinone.jar <systemName> <file_json>(graphe1.json) <port>


###########################
#### 2 Machines ###########

2 machines test : run in 2 terminal :

 java -jar app-1.0-allinone.jar <systemName 1> <file_json>(graphe1.json) <port_1>


 java -jar app-1.0-allinone.jar <systemName 2> <file_json>(graphe2.json) <port_2> make sure that the name system in ghraphe2.json much the name of system of <system 1>


#####################
###SEND MESSAGE######

Just put your message in terminal .



####FILE include : ####
<config> ==> racine 
<tree/graphe..> ==> descreption of the graphe/tree
	

