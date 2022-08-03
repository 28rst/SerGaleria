<?php 
 
	require_once '../includes/DbOperation.php';
	function isTheseParametersAvailable($params){
		$available = true; 
		$missingparams = ""; 
		foreach($params as $param){
			if(!isset($_POST[$param]) || strlen($_POST[$param])<=0){
				$available = false; 
				$missingparams = $missingparams . ", " . $param; 
			}
		}
		if(!$available){
			$response = array(); 
			$response['error'] = true; 
			$response['message'] = 'Parameters ' . substr($missingparams, 1, strlen($missingparams)) . ' missing';
			echo json_encode($response);
			die();		
		}
	}
 
	
	$response = array();	 
	if(isset($_GET['apicall'])){
		switch($_GET['apicall']){
			case 'createAnimal':
				isTheseParametersAvailable(array('especie','nomeComum','sexo','ordem', 'familia', 'classe', 'genero', 'descri'));
				$db = new DbOperation(); 
				$result = $db->createAnimal(
				$_POST['especie'],
				$_POST['nomeComum'],
				$_POST['sexo'],
				$_POST['ordem'],
				$_POST['familia'],
				$_POST['classe'],
				$_POST['genero'],
				$_POST['descri']);
				 
				 
				if($result){
					$response['error'] = false; 
					$response['message'] = 'Animal inserido com sucesso';
					$response['animais'] = $db->getAnimais();
				}else{
					$response['error'] = true; 
					$response['message'] = 'Some error occurred please try again';
				 }
				 
				 break; 

			 case 'getAnimais':
				 $db = new DbOperation();
				 $response['error'] = false; 
				 $response['message'] = 'Request successfully completed';
				 $response['animais'] = $db->getAnimais();
				 break; 
			 
			 
			 case 'updateAnimal':
				 isTheseParametersAvailable(array('id','especie','nomeComum','sexo','ordem'));
				 $db = new DbOperation();
				 $result = $db->updateAnimal(
				 $_POST['id'],
				 $_POST['especie'],
				 $_POST['nomeComum'],
				 $_POST['sexo'],
				 $_POST['ordem'],
				 $_POST['familia'],
				 $_POST['classe'],
				 $_POST['genero'],
				 $_POST['descri']
				 );
				 
				 if($result){
					 $response['error'] = false; 
					 $response['message'] = 'Animal updated com sucesso!';
					 $response['animais'] = $db->getAnimais();
				 }else{
					 $response['error'] = true; 
					 $response['message'] = 'Erro!';
				 }
				 break; 
			 
			 
			 case 'deleteAnimal':
			 
				 if(isset($_GET['id'])){
					 $db = new DbOperation();
					 if($db->deleteAnimal($_GET['id'])){
						 $response['error'] = false; 
						 $response['message'] = 'Animal apagado com sucesso!';
						 $response['animais'] = $db->getAnimais();
					 }else{
						 $response['error'] = true; 
						 $response['message'] = 'Erro!';
					 }
				 }else{
					 $response['error'] = true; 
					 $response['message'] = 'Erro, não há registo para apagar';
				 }
				 break;
				 
			
				 
			case 'createObs':
				isTheseParametersAvailable(array('nome','idAnimal','descricao','ficheiro','longitude','latitude'));
				 //id;nome;idAnimal;descricao;ficheiro;longitude;latitude;utilizador_id
				 
				 $db = new DbOperation();
				 
				 $result = $db->createObs(
				 $_POST['nome'],
				 $_POST['idAnimal'],
				 $_POST['descricao'],
				 $_POST['ficheiro'],
				 $_POST['longitude'],
				 $_POST['latitude']
				 //$_POST['utilizador_id']
				 );
				 
				 
				 if($result){
					 $response['error'] = false; 
					 $response['message'] = 'Observação inserido com sucesso';
					 $response['obs'] = $db->getObs();
				 }else{
					 $response['error'] = true; 
					 $response['message'] = 'Erro na inserção de uma nova observação';
				 }
				break;
				
				case 'getObs':
					 $db = new DbOperation();
					 $response['error'] = false; 
					 $response['message'] = 'Request successfully completed';
					 $response['obs'] = $db->getObs();
				 break; 
			 
			 
			 case 'updateObs':
				 isTheseParametersAvailable(array(''));
				 $db = new DbOperation();
				 $result = $db->updateObs(
				 $_POST['id']
				 //parametros a postar na base de dados, vindos do cliente
				 );
				 
				 if($result){
					 $response['error'] = false; 
					 $response['message'] = 'Observação updated com sucesso!';
					 $response['animais'] = $db->getObs();
				 }else{
					 $response['error'] = true; 
					 $response['message'] = 'Erro!';
				 }
			break; 
				
			case 'deleteObs':
				if(isset($_GET['id'])){
					$db = new DbOperation();
					if($db->deleteObs($_GET['id'])){
						$response['error'] = false; 
						$response['message'] = 'Observação apagado com sucesso!';
						$response['animais'] = $db->getObs();
					}
					else{
						$response['error'] = true; 
						$response['message'] = 'Erro!';
					}
				}else{	 
					$response['error'] = true; 
					$response['message'] = 'Erro...';
				}
			break;
			
			
			case "setAnimalFotos":
				if($_SERVER['REQUEST_METHOD']=='POST'){
					 $image = $_POST['image'];
					 
					 $con = new mysqli('192.168.1.73', 'test', 'test', 'prolserhum');
					 mysqli_set_charset($con, 'utf8');					 
					 $sql = "INSERT INTO fotografiaanimal (caminho) VALUES (?)";
					 
					 $stmt = mysqli_prepare($con,$sql);
					 
					 mysqli_stmt_bind_param($stmt,"s",$image);
					 mysqli_stmt_execute($stmt);
					 
					 $check = mysqli_stmt_affected_rows($stmt);
					 
					 if($check == 1){
						echo "Image Uploaded Successfully";
					 }else{
						echo "Error Uploading Image";
					 }
					 mysqli_close($con);
				}else{
					 echo "Error";
				}
			break;
			
			case "getImage":
				if($_SERVER['REQUEST_METHOD']=='GET'){
					 $id = $_GET['id'];
					 $sql = "select * from fotografiaanimal where id = '$id'";
					 $con = new mysqli('192.168.1.73', 'test', 'test', 'prolserhum');
					 mysqli_set_charset($con, 'utf8');					 
					 $r = mysqli_query($con,$sql);
					 
					 $result = mysqli_fetch_array($r);
					 
					 header('content-type: image/png');
					 
					 echo base64_decode($result['image']);
					 
					 mysqli_free_result();
					 
					 mysqli_close($con);
				}else{
					 echo "Error";
				}
			break;
			
			case "saveImage":
			
			break;
			
			case "getAllImage": 
				 $sql = "SELECT id FROM fotografiaanimal;";
				 $con = new mysqli('192.168.1.73', 'test', 'test', 'prolserhum');
				 mysqli_set_charset($con, 'utf8');
				 $res = mysqli_query($con,$sql);
				 
				 $result = array();
				 
				 $url = "http://e0f2b94d.ngrok.io/prolserhum/Api/Api.php?apicall=getImage?id=";
				 while($row = mysqli_fetch_array($res)){
					array_push($result,array('url'=>$url.$row['id']));
				 }
				 
				 echo json_encode(array("result"=>$result));
				 
				 mysqli_close($con);
			break;
			
			case 'signup':
				
				isTheseParametersAvailable(array('nomeUtilizador', 'email', 'password', 'avatar', 'nivelAcesso', 'nivelPremium', 'imagemBackground'));
					 $username = $_POST['nomeUtilizador']; 
					 $email = $_POST['email']; 
					 //$password = md5($_POST['password']);
					 $password = ($_POST['password']);
					 $avatar = $_POST['avatar'];
					 $nivelAcesso = $_POST['nivelAcesso']; 
					 $nivelPremium = $_POST['nivelPremium']; 
					 $imagemBackground = $_POST['imagemBackground'];
					 $con = new mysqli('192.168.1.73', 'test', 'test', 'prolserhum');
					 mysqli_set_charset($con, 'utf8');
					 
					// $db = new DbOperation(); 
					 $stmt = $con->prepare("SELECT id FROM utilizador WHERE nomeUtilizador = ?;");
					 $stmt->bind_param("s", $username);
					 $stmt->execute();
					 $stmt->store_result();
					 
					 if($stmt->num_rows > 0){
						 $response['error'] = true;
						 $response['message'] = 'Escolha outro nome de utilizador';
						 $stmt->close();
					}else{
						 $stmt = $con->prepare("SELECT id FROM utilizador WHERE email = ?;");
						 $stmt->bind_param("s", $email);
						 $stmt->execute();
						 $stmt->store_result();
						 
						 if($stmt->num_rows > 0){
							 $response['error'] = true;
							 $response['message'] = 'Email já registado';
							 $stmt->close();
						}else{
							 $stmt = $con->prepare("INSERT INTO utilizador (nomeUtilizador, email, password, avatar, nivelAcesso, nivelPremium, imagemBackground) VALUES (?, ?, ?, ?, ?, ?, ?)");
							 $stmt->bind_param("ssssiis", $username, $email, $password, $avatar, $nivelAcesso, $nivelPremium, $imagemBackground);
							 
							 if($stmt->execute()){
							 
								 $stmt = $con->prepare("SELECT id, nomeUtilizador, email, avatar, nivelAcesso, nivelPremium, imagemBackground FROM utilizador WHERE nomeUtilizador = ?"); 
								 $stmt->bind_param("s",$username);
								 $stmt->execute();
								 $stmt->bind_result($id, $username, $email, $avatar, $nivelAcesso, $nivelPremium, $imagemBackground);
								 $stmt->fetch();
								 
								 $user = array(
								 'id'=>$id, 
								 'nomeUtilizador'=>$username, 
								 'email'=>$email,
								 'avatar'=>$avatar,
								 'nivelAcesso'=>$nivelAcesso,
								 'nivelPremium'=>$nivelPremium,
								 'imagemBackground'=>$imagemBackground
								 );
								 
								 $stmt->close();
								 
								 $response['error'] = false; 
								 $response['message'] = 'Criação de conta com sucesso'; 
								 $response['user'] = $user; 
							 }
						}
					 }
				
	 break; 
	 case 'updateUser':
		isTheseParametersAvailable(array('id', 'nomeUtilizador', 'email', 'password', 'avatar', 'nivelAcesso', 'nivelPremium', 'imagemBackground'));
					 $id = $_POST['id'];
					 $username = $_POST['nomeUtilizador']; 
					 $email = $_POST['email']; 
					 //$password = md5($_POST['password']);
					 $password = ($_POST['password']);
					 $avatar = $_POST['avatar'];
					 $nivelAcesso = $_POST['nivelAcesso']; 
					 $nivelPremium = $_POST['nivelPremium']; 
					 $imagemBackground = $_POST['imagemBackground'];
					 
					 $con = new mysqli('192.168.1.73', 'test', 'test', 'prolserhum');
					 mysqli_set_charset($con, 'utf8');
					 
					 
					 $usernameFirst;
					 $emailFirst;
					 $passwordFirst;
					 $stmt = $con->prepare("SELECT nomeUtilizador, email, password FROM utilizador WHERE id = ?"); 
					 $stmt->bind_param("i",$id);
					 $stmt->execute();
					 $stmt->bind_result($usernameFirst, $emailFirst, $passwordFirst);
					 while($stmt->fetch()){
						 $usernameFirst = $usernameFirst;
						 $emailFirst = $emailFirst;
						 $passwordFirst= $passwordFirst;
						 
					 }
					 $stmt->close();
					 
					 //testar
					 //echo $usernameFirst."+".$username."+".$emailFirst."+".$email."+".$passwordFirst."+".$password;
					 
					 if($usernameFirst != $username){
						 // $db = new DbOperation(); 
						 $stmt = $con->prepare("SELECT id FROM utilizador WHERE nomeUtilizador = ?;");
						 $stmt->bind_param("s", $username);
						 $stmt->execute();
						 $stmt->store_result();
						 
						 if($stmt->num_rows > 0){
							 $response['error'] = true;
							 $response['message'] = 'Escolha outro nome de utilizador';
							 $stmt->close();
						 }else{
						if($emailFirst != $email){
							$stmt = $con->prepare("SELECT id FROM utilizador WHERE email = ?;");
							 $stmt->bind_param("s", $email);
							 $stmt->execute();
							 $stmt->store_result();
							 
							 if($stmt->num_rows > 0){
								 $response['error'] = true;
								 $response['message'] = 'Email já registado';
								 $stmt->close();
							 }else{
								//SET column1 = value1, column2 = value2, ... WHERE condition; 
								 $stmt = $con->prepare("UPDATE utilizador 
								 SET nomeUtilizador = ?, email = ?, password = ?, avatar = ?, nivelAcesso = ?, nivelPremium = ?, imagemBackground = ? 
								 WHERE id = ?;");
								 $stmt->bind_param("ssssiisi", $username, $email, $password, $avatar, $nivelAcesso, $nivelPremium, $imagemBackground, $id);
								 
								 if($stmt->execute()){
								 
									 $stmt = $con->prepare("SELECT id, nomeUtilizador, email, avatar, nivelAcesso, nivelPremium, imagemBackground, password FROM utilizador WHERE nomeUtilizador = ?"); 
									 $stmt->bind_param("s",$username);
									 $stmt->execute();
									 $stmt->bind_result($id, $username, $email, $avatar, $nivelAcesso, $nivelPremium, $imagemBackground, $password);
									 while($stmt->fetch()){
										  $user = array(
										 'id'=>$id, 
										 'nomeUtilizador'=>$username, 
										 'email'=>$email,
										 'avatar'=>$avatar,
										 'nivelAcesso'=>$nivelAcesso,
										 'nivelPremium'=>$nivelPremium,
										 'imagemBackground'=>$imagemBackground,
										 'password'=>$password
										);
									 }
									 
									 $stmt->close();
									 
									 $response['error'] = false; 
									 $response['message'] = 'Update de conta com sucesso'; 
									 $response['user'] = $user; 
								 }
							}
						}else{
								//SET column1 = value1, column2 = value2, ... WHERE condition; 
								 $stmt = $con->prepare("UPDATE utilizador 
								 SET nomeUtilizador = ?, email = ?, password = ?, avatar = ?, nivelAcesso = ?, nivelPremium = ?, imagemBackground = ? 
								 WHERE id = ?;");
								 $stmt->bind_param("ssssiisi", $username, $email, $password, $avatar, $nivelAcesso, $nivelPremium, $imagemBackground, $id);
								 
								 if($stmt->execute()){
								 
									 $stmt = $con->prepare("SELECT id, nomeUtilizador, email, avatar, nivelAcesso, nivelPremium, imagemBackground, password FROM utilizador WHERE nomeUtilizador = ?"); 
									 $stmt->bind_param("s",$username);
									 $stmt->execute();
									 $stmt->bind_result($id, $username, $email, $avatar, $nivelAcesso, $nivelPremium, $imagemBackground, $password);
									 while($stmt->fetch()){
										  $user = array(
										 'id'=>$id, 
										 'nomeUtilizador'=>$username, 
										 'email'=>$email,
										 'avatar'=>$avatar,
										 'nivelAcesso'=>$nivelAcesso,
										 'nivelPremium'=>$nivelPremium,
										 'imagemBackground'=>$imagemBackground,
										 'password'=>$password
										);
									 }
									 
									 $stmt->close();
									 
									 $response['error'] = false; 
									 $response['message'] = 'Update de conta com sucesso'; 
									 $response['user'] = $user; 
								 }
							}
						 
					 }
		}else{
						if($emailFirst != $email){
							$stmt = $con->prepare("SELECT id FROM utilizador WHERE email = ?;");
							 $stmt->bind_param("s", $email);
							 $stmt->execute();
							 $stmt->store_result();
							 
							 if($stmt->num_rows > 0){
								 $response['error'] = true;
								 $response['message'] = 'Email já registado';
								 $stmt->close();
							 }else{
								//SET column1 = value1, column2 = value2, ... WHERE condition; 
								 $stmt = $con->prepare("UPDATE utilizador 
								 SET nomeUtilizador = ?, email = ?, password = ?, avatar = ?, nivelAcesso = ?, nivelPremium = ?, imagemBackground = ? 
								 WHERE id = ?;");
								 $stmt->bind_param("ssssiisi", $username, $email, $password, $avatar, $nivelAcesso, $nivelPremium, $imagemBackground, $id);
								 
								 if($stmt->execute()){
								 
									 $stmt = $con->prepare("SELECT id, nomeUtilizador, email, avatar, nivelAcesso, nivelPremium, imagemBackground, password FROM utilizador WHERE nomeUtilizador = ?"); 
									 $stmt->bind_param("s",$username);
									 $stmt->execute();
									 $stmt->bind_result($id, $username, $email, $avatar, $nivelAcesso, $nivelPremium, $imagemBackground, $password);
									 while($stmt->fetch()){
										  $user = array(
										 'id'=>$id, 
										 'nomeUtilizador'=>$username, 
										 'email'=>$email,
										 'avatar'=>$avatar,
										 'nivelAcesso'=>$nivelAcesso,
										 'nivelPremium'=>$nivelPremium,
										 'imagemBackground'=>$imagemBackground,
										 'password'=>$password
										);
									 }
									 
									 $stmt->close();
									 
									 $response['error'] = false; 
									 $response['message'] = 'Update de conta com sucesso'; 
									 $response['user'] = $user; 
								 }
							}
						}
						 
					 }
					 break;
	 case 'login':
		 isTheseParametersAvailable(array('nomeUtilizador', 'password'));
		 $username = $_POST['nomeUtilizador'];
		 //$password = md5($_POST['password']); 
		 $password = ($_POST['password']); 
		 $con = new mysqli('192.168.1.73', 'test', 'test', 'prolserhum');
		 mysqli_set_charset($con, 'utf8');
		 $stmt = $con->prepare("SELECT id, nomeUtilizador, email, avatar, nivelAcesso, nivelPremium, imagemBackground, password FROM utilizador WHERE nomeUtilizador = ? AND password = ?");
		 $stmt->bind_param("ss", $username, $password);
		 $stmt->execute();
		 $stmt->store_result();
		 if($stmt->num_rows > 0){
			 $stmt->bind_result($id, $username, $email, $avatar, $nivelAcesso, $nivelPremium, $imagemBackground, $password);
			 while($stmt->fetch()){
				 
				 $user = array(
				 'id'=>$id, 
				 'nomeUtilizador'=>$username,
				 'avatar'=>$avatar,	
				 'nivelAcesso'=>$nivelAcesso,
				 'nivelPremium'=>$nivelPremium,			 
				 'email'=>$email,
				 'imagemBackground'=>$imagemBackground,
				 'password'=>$password
				 );
				 
			 }
			 $stmt->close();
			 /*
			 
			 int id, String 
			 nomeUtilizador, String avatar,
                      int nivelAcesso, int nivelPremium,
                      String email, String imagemBackground) {
			 
			 */
			 
			 
		 
			 $response['error'] = false; 
			 $response['message'] = 'Sessão iniciada com sucesso'; 
			 $response['user'] = $user; 
		}else{
			 $response['error'] = true; 
			 $response['message'] = $username.' - ' . $password .'Verifique os seus dados';
		}
		
	 
	 break;
			
		
	}
	}else{
		 $response['error'] = true; 
		 $response['message'] = 'Api call errada ou nao existente';
	}
		
echo json_encode($response);