<?php
 
class DbOperation
{
    public $con;
 
    function __construct()
    {
        require_once dirname(__FILE__) . '/DbConnect.php';
         $db = new DbConnect();
 
        $this->con = $db->connect();
    }
 

	 function createAnimal($especie, $nomeComum, $sexo, $ordem, $familia, $classe, $genero, $descri){
		 $stmt = $this->con->prepare("INSERT INTO animal (especie, nomeComum, sexo, ordem, familia, classe, genero, descri) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
		 $stmt->bind_param("ssssssss", $especie, $nomeComum, $sexo, $ordem, $familia, $classe, $genero, $descri);
		 if($stmt->execute())
			return true;
		
		 return false;
	 }
	 
		
	 function getAnimais(){
		 $stmt = $this->con->prepare("SELECT id, especie, nomeComum, sexo, ordem, familia, classe, genero, descri FROM animal;");
		 mysqli_set_charset($this->con, 'utf8');
		 $stmt->execute();
		 $stmt->bind_result($id, $especie, $nomeComum, $sexo, $ordem, $familia, $classe, $genero, $descri);
		 
		 $animal = array(); 
		 
		 while($stmt->fetch()){
			 $anim  = array();
			 $anim['id'] = $id; 
			 $anim['especie'] = $especie; 
			 $anim['nomeComum'] = $nomeComum; 
			 $anim['sexo'] = $sexo; 
			 $anim['ordem'] = $ordem;
			 $anim['familia'] = $familia;
			 $anim['classe'] = $classe;
			 $anim['genero'] = $genero;
			 $anim['descri'] = $descri;
			 array_push($animal, $anim);
		 }
		 
			return $animal; 									
	 }
	 
	  
	 function updateAnimal($id, $especie, $nomeComum, $sexo, $ordem, $familia, $classe, $genero, $descri){
		 $stmt = $this->con->prepare("UPDATE animal SET especie = ?, nomeComum = ?, sexo = ?, ordem = ?, familia = ?, classe = ?, genero = ?, descri = ? WHERE id = ?");
		 $stmt->bind_param("sssssssi", $especie, $nomeComum, $sexo, $ordem, $familia, $classe, $genero, $descri, $id);
		 if($stmt->execute())
			return true; 
		 return false; 
	 }
	 
	 
	 /*
	 * The delete operation
	 * When this method is called record is deleted for the given id 
	 */
	 function deleteAnimal($id){
		 $stmt = $this->con->prepare("DELETE FROM animal WHERE id = ? ");
		 $stmt->bind_param("i", $id);
		 if($stmt->execute())
			return true; 
		 
		 return false; 
	 }
	 
	 
	 //---------OBservações-----------------//
	
	function createObs($nome, $idAnimal, $descricao, $ficheiro, $longitude, $latitude){
		$stmt = $this->con->prepare("INSERT INTO observacao (nome, idAnimal, descricao, ficheiro, longitude, latitude) VALUES (?, ?, ?, ?, ?, ?)");
		$stmt->bind_param("sissdd", $nome, $idAnimal, $descricao, $ficheiro, $longitude, $latitude);
		if($stmt->execute())
			return true;
		
		return false;
		
	}
	
	function getObs(){
		 $stmt = $this->con->prepare("SELECT id, nome, idAnimal, descricao, ficheiro, longitude, latitude FROM observacao");
		 mysqli_set_charset($this->con, 'utf8');
		 $stmt->execute();
		 $stmt->bind_result($id, $nome, $idAnimal, $descricao, $ficheiro, $longitude, $latitude);
		 
		 $obs = array(); 
		 
		 while($stmt->fetch()){
			 $observa  = array();
			 $observa['id'] = $id; 
			 $observa['nome'] = $nome; 
			 $observa['idAnimal'] = $idAnimal; 
			 $observa['descricao'] = $descricao; 
			 $observa['ficheiro'] = $ficheiro; 
			 $observa['longitude'] = $longitude;
			 $observa['latitude'] = $latitude;
			 array_push($obs, $observa);
		 }
		 
			return $obs;								
	 }
	 
	 function updateObs($id, $nome, $idAnimal, $descricao, $ficheiro, $longitude, $latitude){
		 $stmt = $this->con->prepare("UPDATE observacao SET nome = ?, idAnimal = ?, descricao = ?, longitude = ?, latitude = ? WHERE id = ?");
		 $stmt->bind_param("ssssddi", $nome, $idAnimal, $descricao, $ficheiro, $longitude, $latitude, $id);
		 if($stmt->execute())
			return true; 
		 return false; 
	 }
	
	 function deleteObs($id){
		 $stmt = $this->con->prepare("DELETE FROM observacao WHERE id = ? ");
		 $stmt->bind_param("i", $id);
		 if($stmt->execute())
			return true; 
		 
		 return false; 
	 }
	 
	 
	 //------------------------------Utilizadores----------------//
	 
	 function createUtilizador($nomeUtilizador, $password, $avatar, $email, $nivelAcesso, $nivelPremium, $imagemBackground){
		 $stmt = $this->con->prepare("INSERT INTO utilizador (nomeUtilizador, password, avatar, email, nivelAcesso, nivelPremium, imagemBackground) VALUES (?, ?, ?, ?, ?, ?, ?)");
		 $stmt->bind_param("ssssssss", $nomeUtilizador, $password, $avatar, $email, $nivelAcesso, $nivelPremium, $imagemBackground);
		 if($stmt->execute())
			return true;
		
		 return false;
	 }
	 
		
	 function getUtilizadores(){
		 $stmt = $this->con->prepare("SELECT id, nomeUtilizador, password, avatar, email, nivelAcesso, nivelPremium, imagemBackground FROM utilizador");
		 mysqli_set_charset($this->con, 'utf8');
		 $stmt->execute();
		 $stmt->bind_result($id, $nomeUtilizador, $password, $avatar, $email, $nivelAcesso, $nivelPremium, $imagemBackground);
		 
		 $utilizador = array(); 
		 
		 while($stmt->fetch()){
			 $utili  = array();
			 $utili['id'] = $id; 
			 $utili['nomeUtilizador'] = $nomeUtilizador; 
			 $utili['password'] = $password; 
			 $utili['avatar'] = $avatar; 
			 $utili['email'] = $email; 
			 $utili['nivelAcesso'] = $nivelAcesso;
			 $utili['nivelPremium'] = $nivelPremium;
			 $utili['imagemBackground'] = $imagemBackground;
			 array_push($utilizador, $utili);
		 }
		 return $utilizador; 									
	 }
	 
	 /*
	 * The update operation
	 * When this method is called the record with the given id is updated with the new given values
	 */
	 function updateUtilizador($id, $nomeUtilizador, $password, $avatar, $email, $nivelAcesso, $nivelPremium, $imagemBackground){
		 $stmt = $this->con->prepare("UPDATE utilizador SET nomeUtilizador = ?, password = ?, avatar = ?, email = ?, nivelAcesso = ?, nivelPremium = ?, imagemBackground = ? WHERE id = ?");
		 $stmt->bind_param("ssssiisi", $nomeUtilizador, $password, $avatar, $email, $nivelAcesso, $nivelPremium, $imagemBackground, $id);
		 if($stmt->execute())
			return true; 
		 return false; 
	 }
	 
	
	
	 function deleteUtilizador($id){
		 $stmt = $this->con->prepare("DELETE FROM utilizador WHERE id = ? ");
		 $stmt->bind_param("i", $id);
		 if($stmt->execute())
			return true; 
		 
		 return false; 
	 }
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
}