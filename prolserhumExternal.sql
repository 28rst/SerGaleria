-- phpMyAdmin SQL Dump
-- version 4.7.9
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: 31-Maio-2018 às 12:36
-- Versão do servidor: 10.1.31-MariaDB
-- PHP Version: 7.2.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `prolserhum`
--

-- --------------------------------------------------------

--
-- Estrutura da tabela `animal`
--

CREATE TABLE `animal` (
  `id` int(8) NOT NULL,
  `especie` varchar(200) NOT NULL,
  `nomeComum` varchar(200) NOT NULL,
  `sexo` char(2) NOT NULL,
  `reino` varchar(100) NOT NULL DEFAULT 'Animalia',
  `ordem` varchar(100) NOT NULL,
  `familia` varchar(100) NOT NULL,
  `classe` varchar(45) NOT NULL,
  `genero` varchar(45) NOT NULL,
  `descri` varchar(500) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `animal`
--

INSERT INTO `animal` (`id`, `especie`, `nomeComum`, `sexo`, `reino`, `ordem`, `familia`, `classe`, `genero`, `descri`) VALUES
(1, 'Tigre Sabre', 'Tigre dentes de Sabre', 'M', 'Animalia', 'asasas', 'asas', 'Mamíferos', 'asas', 'asas'),
(4, 'Cão\r\n', 'cão', 'Ma', 'Animalia', 'asas', 'asas', 'Mammalia - Mamíferos', 'asas', 'asas'),
(11, 'raposa', 'raposa', 'Ma', 'Animalia', 'asas', 'asas', 'Aves - Aves', 'asas', 'Asinhas'),
(12, 'pardal', 'pardal', 'Ma', 'Animalia', 'asas', 'asas', 'Aves - Aves', 'asas', 'Asinhas'),
(14, 'Leão', 'Leão', 'Ma', 'Animalia', 'asas', 'asas', 'Mammalia - Mamíferos', 'asas', 'asas'),
(16, 'rã verde\r\n', 'rã verde', 'Fê', 'Animalia', 'asas', 'asas', 'Amphibia - anfíbios', 'asas', 'asas'),
(18, 'Andorinha caseira\r\n', 'Andorinha', 'Fê', 'Animalia', 'asas', 'asas', 'Aves - Aves', 'asas', 'asas'),
(29, 'peixe balão', 'peixe balão', 'Fê', 'Animalia', 'asas', 'asas', 'Actinopterygii - peixes ósseos', 'asas', 'asas'),
(32, 'Teste 1', 'testeahahah', 'Fê', 'Animalia', 'asas', 'asas', 'Amphibia - anfíbios', 'asas', 'asas'),
(33, 'escamogato voador', 'gato com asas', 'Ma', 'Animalia', 'asas', 'asas', 'Aves - Aves', 'asas', 'asas'),
(35, 'Cão\r\n', 'cão', 'Ma', 'Animalia', 'asas', 'asas', 'Mammalia - Mamíferos', 'asas', 'asas'),
(37, 'pardal', 'pardal', 'Ma', 'Animalia', 'asas', 'asas', 'Aves - Aves', 'asas', 'Asinhas'),
(38, 'Leão', 'Leão', 'Ma', 'Animalia', 'asas', 'asas', 'Mammalia - Mamíferos', 'asas', 'asas'),
(39, 'rã verde\r\n', 'rã verde', 'Fê', 'Animalia', 'asas', 'asas', 'Amphibia - anfíbios', 'asas', 'asas'),
(40, 'Andorinha caseira\r\n', 'Andorinha', 'Fê', 'Animalia', 'asas', 'asas', 'Aves - Aves', 'asas', 'asas'),
(41, 'Asas\r\n', 'Asas', 'Ma', 'Animalia', 'asas', 'asas', 'Aves - Aves', 'asas', 'Asinhas'),
(42, 'Qwery\r\n', 'Qwert', 'Ma', 'Animalia', 'asas', 'asas', 'Aves - Aves', 'asas', 'Asinhas'),
(43, 'whaah\r\n', 'ahah', 'Ma', 'Animalia', 'asas', 'asas', 'Aves - Aves', 'asas', 'Asinhas'),
(44, 'ahahah\r\n', 'ahahah', 'Ma', 'Animalia', 'asas', 'asas', 'Aves - Aves', 'asas', 'Asinhas'),
(45, 'peixe balão', 'peixe balão', 'Fê', 'Animalia', 'asas', 'asas', 'Actinopterygii - peixes ósseos', 'asas', 'asas');

-- --------------------------------------------------------

--
-- Estrutura da tabela `ficheiroprova_copy1`
--

CREATE TABLE `ficheiroprova_copy1` (
  `idProva` int(11) NOT NULL,
  `caminho` varchar(400) NOT NULL,
  `observacao_id` int(11) NOT NULL,
  `observacao_utilizador_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Estrutura da tabela `fotografiaanimal`
--

CREATE TABLE `fotografiaanimal` (
  `idFoto` int(11) NOT NULL,
  `caminho` varchar(400) NOT NULL,
  `animal_id` int(8) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Extraindo dados da tabela `fotografiaanimal`
--

INSERT INTO `fotografiaanimal` (`idFoto`, `caminho`, `animal_id`) VALUES
(1, 'D:\\xampp\\htdocs\\prolserhum\\img\\Tigre Sabre', 1);

-- --------------------------------------------------------

--
-- Estrutura da tabela `fotografiaobservacao`
--

CREATE TABLE `fotografiaobservacao` (
  `idFoto` int(11) NOT NULL,
  `caminho` varchar(400) NOT NULL,
  `observacao_id` int(11) NOT NULL,
  `observacao_utilizador_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Estrutura da tabela `observacao`
--

CREATE TABLE `observacao` (
  `id` int(11) NOT NULL,
  `nome` varchar(100) NOT NULL,
  `descricao` varchar(400) NOT NULL,
  `ficheiro` varchar(400) DEFAULT NULL,
  `longitude` double NOT NULL,
  `latitude` double NOT NULL,
  `utilizador_id` int(11) NOT NULL,
  `idAnimal` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `observacao`
--

INSERT INTO `observacao` (`id`, `nome`, `descricao`, `ficheiro`, `longitude`, `latitude`, `utilizador_id`, `idAnimal`) VALUES
(9, 'tfg', 'ggg', 'asas', -2.636725455522537, 38.410565344259545, 6, 1),
(10, 'ahah', 'bacj', 'asas', -11.991973444819449, 10.786493196099736, 6, 1),
(11, 'ggg', 'ggg', 'asas', -8.680724389851095, 37.1093410127786, 6, 1),
(12, 'asas', 'asas', 'asas', -8.681699708104134, 37.109254649675655, 6, 1),
(14, 'ahhaha', 'abbaha', 'asas', -8.67966927587986, 37.10503182835369, 6, 1);

-- --------------------------------------------------------

--
-- Estrutura da tabela `observacao_has_animal`
--

CREATE TABLE `observacao_has_animal` (
  `observacao_id` int(11) NOT NULL,
  `observacao_utilizador_id` int(11) NOT NULL,
  `animal_id` int(8) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estrutura da tabela `preferencia`
--

CREATE TABLE `preferencia` (
  `id` int(11) NOT NULL,
  `nome` varchar(200) NOT NULL,
  `utilizador_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Estrutura da tabela `utilizador`
--

CREATE TABLE `utilizador` (
  `id` int(11) NOT NULL,
  `nomeUtilizador` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `avatar` varchar(200) NOT NULL,
  `email` varchar(45) NOT NULL,
  `nivelAcesso` int(11) NOT NULL,
  `nivelPremium` int(11) NOT NULL,
  `imagemBackground` varchar(400) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `utilizador`
--

INSERT INTO `utilizador` (`id`, `nomeUtilizador`, `password`, `avatar`, `email`, `nivelAcesso`, `nivelPremium`, `imagemBackground`) VALUES
(1, 'Admin', 'admin', 'null', 'admin@admin.pt', 1, 0, 'null'),
(2, 'Joao', 'joao', 'null', 'joai@joio.com', 1, 0, 'null'),
(3, 'hugoPinto', 'hugo', 'null', 'huho@hugo.com', 1, 0, 'null'),
(4, 'Manel', 'manel123', 'null', 'manel@algures.com', 1, 0, 'null'),
(5, 'bianca', 'asas', 'null', 'bianca@bainca.com', 1, 0, 'null'),
(6, 'Asas', 'asas', 'null', 'asas@asas.com', 1, 0, 'null'),
(7, 'user1', 'user', 'null', 'user@user.com', 1, 0, 'null'),
(8, 'Qwerty', 'querty', 'null', 'querty@qiery.com', 1, 0, 'null'),
(9, 'teste', 'teste', 'null', 'teste@teste.com', 1, 0, 'null');

-- --------------------------------------------------------

--
-- Estrutura da tabela `utilizador_has_animal`
--

CREATE TABLE `utilizador_has_animal` (
  `utilizador_id` int(11) NOT NULL,
  `animal_id` int(8) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `animal`
--
ALTER TABLE `animal`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `ficheiroprova_copy1`
--
ALTER TABLE `ficheiroprova_copy1`
  ADD PRIMARY KEY (`idProva`),
  ADD KEY `fk_ficheiroProva_observacao1_idx` (`observacao_id`,`observacao_utilizador_id`);

--
-- Indexes for table `fotografiaanimal`
--
ALTER TABLE `fotografiaanimal`
  ADD PRIMARY KEY (`idFoto`,`animal_id`),
  ADD KEY `fk_fotografiaObservacao_copy1_animal1_idx` (`animal_id`);

--
-- Indexes for table `fotografiaobservacao`
--
ALTER TABLE `fotografiaobservacao`
  ADD PRIMARY KEY (`idFoto`,`observacao_id`,`observacao_utilizador_id`),
  ADD KEY `fk_fotografiaObservacao_observacao1_idx` (`observacao_id`,`observacao_utilizador_id`);

--
-- Indexes for table `observacao`
--
ALTER TABLE `observacao`
  ADD PRIMARY KEY (`id`,`utilizador_id`),
  ADD KEY `fk_observacao_utilizador_idx` (`utilizador_id`),
  ADD KEY `idAnimal` (`idAnimal`);

--
-- Indexes for table `observacao_has_animal`
--
ALTER TABLE `observacao_has_animal`
  ADD PRIMARY KEY (`observacao_id`,`observacao_utilizador_id`,`animal_id`),
  ADD KEY `fk_observacao_has_animal_animal1_idx` (`animal_id`),
  ADD KEY `fk_observacao_has_animal_observacao1_idx` (`observacao_id`,`observacao_utilizador_id`);

--
-- Indexes for table `preferencia`
--
ALTER TABLE `preferencia`
  ADD PRIMARY KEY (`id`,`utilizador_id`),
  ADD KEY `fk_preferencia_utilizador1_idx` (`utilizador_id`);

--
-- Indexes for table `utilizador`
--
ALTER TABLE `utilizador`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `utilizador_has_animal`
--
ALTER TABLE `utilizador_has_animal`
  ADD PRIMARY KEY (`utilizador_id`,`animal_id`),
  ADD KEY `fk_utilizador_has_animal_animal1_idx` (`animal_id`),
  ADD KEY `fk_utilizador_has_animal_utilizador1_idx` (`utilizador_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `animal`
--
ALTER TABLE `animal`
  MODIFY `id` int(8) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=54;

--
-- AUTO_INCREMENT for table `ficheiroprova_copy1`
--
ALTER TABLE `ficheiroprova_copy1`
  MODIFY `idProva` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `fotografiaanimal`
--
ALTER TABLE `fotografiaanimal`
  MODIFY `idFoto` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `fotografiaobservacao`
--
ALTER TABLE `fotografiaobservacao`
  MODIFY `idFoto` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `observacao`
--
ALTER TABLE `observacao`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT for table `observacao_has_animal`
--
ALTER TABLE `observacao_has_animal`
  MODIFY `observacao_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `preferencia`
--
ALTER TABLE `preferencia`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `utilizador`
--
ALTER TABLE `utilizador`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `utilizador_has_animal`
--
ALTER TABLE `utilizador_has_animal`
  MODIFY `utilizador_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Limitadores para a tabela `ficheiroprova_copy1`
--
ALTER TABLE `ficheiroprova_copy1`
  ADD CONSTRAINT `fk_ficheiroProva_observacao10` FOREIGN KEY (`observacao_id`,`observacao_utilizador_id`) REFERENCES `observacao` (`id`, `utilizador_id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Limitadores para a tabela `fotografiaanimal`
--
ALTER TABLE `fotografiaanimal`
  ADD CONSTRAINT `fk_fotografiaObservacao_copy1_animal1` FOREIGN KEY (`animal_id`) REFERENCES `animal` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Limitadores para a tabela `fotografiaobservacao`
--
ALTER TABLE `fotografiaobservacao`
  ADD CONSTRAINT `fk_fotografiaObservacao_observacao1` FOREIGN KEY (`observacao_id`,`observacao_utilizador_id`) REFERENCES `observacao` (`id`, `utilizador_id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Limitadores para a tabela `observacao`
--
ALTER TABLE `observacao`
  ADD CONSTRAINT `fk_observacao_idAnimal` FOREIGN KEY (`idAnimal`) REFERENCES `animal` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_observacao_utilizador` FOREIGN KEY (`utilizador_id`) REFERENCES `utilizador` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Limitadores para a tabela `observacao_has_animal`
--
ALTER TABLE `observacao_has_animal`
  ADD CONSTRAINT `fk_observacao_has_animal_animal1` FOREIGN KEY (`animal_id`) REFERENCES `animal` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_observacao_has_animal_observacao1` FOREIGN KEY (`observacao_id`,`observacao_utilizador_id`) REFERENCES `observacao` (`id`, `utilizador_id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Limitadores para a tabela `preferencia`
--
ALTER TABLE `preferencia`
  ADD CONSTRAINT `fk_preferencia_utilizador1` FOREIGN KEY (`utilizador_id`) REFERENCES `utilizador` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Limitadores para a tabela `utilizador_has_animal`
--
ALTER TABLE `utilizador_has_animal`
  ADD CONSTRAINT `fk_utilizador_has_animal_animal1` FOREIGN KEY (`animal_id`) REFERENCES `animal` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_utilizador_has_animal_utilizador1` FOREIGN KEY (`utilizador_id`) REFERENCES `utilizador` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
