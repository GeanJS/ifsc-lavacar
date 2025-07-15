-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Tempo de geração: 06-Dez-2023 às 06:41
-- Versão do servidor: 8.0.31
-- versão do PHP: 8.0.26

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Banco de dados: `db_lavacao`
--

-- --------------------------------------------------------

--
-- Estrutura da tabela `cliente`
--

DROP TABLE IF EXISTS `cliente`;
CREATE TABLE IF NOT EXISTS `cliente` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(50) NOT NULL,
  `telefone` varchar(50) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `data_cadastro` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estrutura da tabela `cor`
--

DROP TABLE IF EXISTS `cor`;
CREATE TABLE IF NOT EXISTS `cor` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estrutura da tabela `item_os`
--

DROP TABLE IF EXISTS `item_os`;
CREATE TABLE IF NOT EXISTS `item_os` (
  `id` int NOT NULL AUTO_INCREMENT,
  `valor_servico` float DEFAULT NULL,
  `observacao` varchar(200) DEFAULT NULL,
  `id_servico` int NOT NULL,
  `id_ordemServico` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_itemOS_servico` (`id_servico`),
  KEY `fk_itemOS_ordemServico` (`id_ordemServico`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estrutura da tabela `marca`
--

DROP TABLE IF EXISTS `marca`;
CREATE TABLE IF NOT EXISTS `marca` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estrutura da tabela `modelo`
--

DROP TABLE IF EXISTS `modelo`;
CREATE TABLE IF NOT EXISTS `modelo` (
  `id` int NOT NULL AUTO_INCREMENT,
  `descricao` varchar(200) DEFAULT NULL,
  `id_marca` int NOT NULL,
  `categoria` enum('PEQUENO','MEDIO','GRANDE','MOTO','PADRAO') NOT NULL DEFAULT 'PADRAO',
  PRIMARY KEY (`id`),
  KEY `fk_modelo_marca` (`id_marca`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estrutura da tabela `motor`
--

DROP TABLE IF EXISTS `motor`;
CREATE TABLE IF NOT EXISTS `motor` (
  `id_modelo` int NOT NULL,
  `potencia` float NOT NULL DEFAULT '1',
  `combustivel` enum('GASOLINA','ETANOL','FLEX','DIESEL','GNV','OUTRO') NOT NULL DEFAULT 'GASOLINA',
  PRIMARY KEY (`id_modelo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estrutura da tabela `ordem_servico`
--

DROP TABLE IF EXISTS `ordem_servico`;
CREATE TABLE IF NOT EXISTS `ordem_servico` (
  `numero` int NOT NULL AUTO_INCREMENT,
  `total` decimal(10,0) DEFAULT NULL,
  `agenda` date DEFAULT NULL,
  `desconto` float DEFAULT NULL,
  `status` enum('ABERTA','FECHADA','CANCELADA') NOT NULL DEFAULT 'ABERTA',
  `id_veiculo` int NOT NULL,
  PRIMARY KEY (`numero`),
  KEY `fk_ordemServico_veiculo` (`id_veiculo`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estrutura da tabela `pessoafisica`
--

DROP TABLE IF EXISTS `pessoafisica`;
CREATE TABLE IF NOT EXISTS `pessoafisica` (
  `id_cliente` int NOT NULL,
  `cpf` varchar(20) NOT NULL,
  `data_nascimento` date DEFAULT NULL,
  PRIMARY KEY (`id_cliente`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estrutura da tabela `pessoajuridica`
--

DROP TABLE IF EXISTS `pessoajuridica`;
CREATE TABLE IF NOT EXISTS `pessoajuridica` (
  `id_cliente` int NOT NULL,
  `cnpj` varchar(20) NOT NULL,
  `inscricao_estadual` int NOT NULL,
  PRIMARY KEY (`id_cliente`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estrutura da tabela `pontuacao`
--

DROP TABLE IF EXISTS `pontuacao`;
CREATE TABLE IF NOT EXISTS `pontuacao` (
  `id_cliente` int NOT NULL,
  `quantidade` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id_cliente`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estrutura da tabela `servico`
--

DROP TABLE IF EXISTS `servico`;
CREATE TABLE IF NOT EXISTS `servico` (
  `id` int NOT NULL AUTO_INCREMENT,
  `descricao` varchar(200) DEFAULT NULL,
  `valor` float NOT NULL DEFAULT '0',
  `ponto` int NOT NULL DEFAULT '0',
  `categoria` enum('PEQUENO','MEDIO','GRANDE','MOTO','PADRAO') NOT NULL DEFAULT 'PADRAO',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estrutura da tabela `veiculo`
--

DROP TABLE IF EXISTS `veiculo`;
CREATE TABLE IF NOT EXISTS `veiculo` (
  `id` int NOT NULL AUTO_INCREMENT,
  `placa` varchar(50) NOT NULL,
  `observacoes` varchar(200) DEFAULT NULL,
  `id_modelo` int NOT NULL,
  `id_cor` int NOT NULL,
  `id_cliente` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_veiculo_modelo` (`id_modelo`),
  KEY `fk_veiculo_cor` (`id_cor`),
  KEY `fk_veiculo_cliente` (`id_cliente`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Restrições para despejos de tabelas
--

--
-- Limitadores para a tabela `item_os`
--
ALTER TABLE `item_os`
  ADD CONSTRAINT `fk_itemOS_ordemServico` FOREIGN KEY (`id_ordemServico`) REFERENCES `ordem_servico` (`numero`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_itemOS_servico` FOREIGN KEY (`id_servico`) REFERENCES `servico` (`id`);

--
-- Limitadores para a tabela `modelo`
--
ALTER TABLE `modelo`
  ADD CONSTRAINT `fk_modelo_marca` FOREIGN KEY (`id_marca`) REFERENCES `marca` (`id`);

--
-- Limitadores para a tabela `motor`
--
ALTER TABLE `motor`
  ADD CONSTRAINT `fk_motor_modelo` FOREIGN KEY (`id_modelo`) REFERENCES `modelo` (`id`) ON DELETE CASCADE;

--
-- Limitadores para a tabela `ordem_servico`
--
ALTER TABLE `ordem_servico`
  ADD CONSTRAINT `fk_ordemServico_veiculo` FOREIGN KEY (`id_veiculo`) REFERENCES `veiculo` (`id`);

--
-- Limitadores para a tabela `pessoafisica`
--
ALTER TABLE `pessoafisica`
  ADD CONSTRAINT `fk_fisica_cliente` FOREIGN KEY (`id_cliente`) REFERENCES `cliente` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Limitadores para a tabela `pessoajuridica`
--
ALTER TABLE `pessoajuridica`
  ADD CONSTRAINT `fk_juridica_cliente` FOREIGN KEY (`id_cliente`) REFERENCES `cliente` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Limitadores para a tabela `pontuacao`
--
ALTER TABLE `pontuacao`
  ADD CONSTRAINT `fk_pontuacao_cliente` FOREIGN KEY (`id_cliente`) REFERENCES `cliente` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Limitadores para a tabela `veiculo`
--
ALTER TABLE `veiculo`
  ADD CONSTRAINT `fk_veiculo_cliente` FOREIGN KEY (`id_cliente`) REFERENCES `cliente` (`id`),
  ADD CONSTRAINT `fk_veiculo_cor` FOREIGN KEY (`id_cor`) REFERENCES `cor` (`id`),
  ADD CONSTRAINT `fk_veiculo_modelo` FOREIGN KEY (`id_modelo`) REFERENCES `modelo` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
