<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns="http://www.w3.org/1999/xhtml">

	<xsl:output 
        method="html"
        encoding="iso-8859-1"
        doctype-public="-//W3C//DTD HTML 4.01//EN"
        doctype-system="http://www.w3.org/TR/html4/strict.dtd"
        indent="yes" />
	
	<xsl:key name="idindiv" match="INDI" use="@id"/>
	<xsl:key name="idfam" match="FAM" use="@id"/>
	<xsl:template match="/">
		<html>
			<head>
				<title>FAMILLE ROYALE D'ANGLETERRE</title>
				<link href="Royal.css" rel="stylesheet" type="text/css"/>
			</head>
			<body>
				<h1>SOMMAIRE</h1>
				<ul>
						<xsl:apply-templates select="ROYAL/INDI" mode="index">
							<xsl:sort select="NAME" order="ascending"/>
						</xsl:apply-templates>
				</ul>

				<h1>LISTE DES FAMILLES ROYALES</h1>
				<ul>
					<xsl:apply-templates select="ROYAL/FAM">
						<xsl:sort select="@id" data-type="number" order="ascending"/>
					</xsl:apply-templates>
				</ul>

				<h1>Liste des Personnes</h1>
				<ul>
					<xsl:apply-templates select="ROYAL/INDI" mode="detail">
						<xsl:sort select="NAME" order="ascending"/>
					</xsl:apply-templates>
				</ul>
			</body>
		</html>
	</xsl:template>
	
	<xsl:template match="INDI" mode="index">
		<li>
			<a href="#{@id}">
				<b>
					<xsl:value-of select="translate(translate(NAME,'/',''),'_',' ')"/>
				</b>
			</a>
		</li>
	</xsl:template>

	<xsl:template match="INDI" mode="detail">
		<li>
			<div id="{@id}"></div>
			<a href="#{FAMC/@id}">
				<b>
					<xsl:value-of select="translate(translate(NAME,'/',''),'_',' ')"/>
				</b>
			</a>
			<ul>
				<xsl:apply-templates select="NAME|TITL|SEX"/>
				<xsl:apply-templates select="BIRT|DEAT|BURI|CHR"/>
				<xsl:choose>
					<xsl:when test="contains(SEX,'F')">
						<xsl:apply-templates select="key('idfam', current()/FAMS/@id)/HUSB"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:apply-templates select="key('idfam', current()/FAMS/@id)/WIFE"/>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:apply-templates select="key('idfam', current()/FAMS/@id)/CHIL"/>
			</ul>
		</li>
	</xsl:template>
	
	<xsl:template match="FAM">
		<li>
				<div id="{@id}"></div>
				<b>Famille:  <xsl:value-of select="@id"/></b>
				<ul>
					<xsl:apply-templates select="HUSB|WIFE|CHIL|MARR|DIV"/>
				</ul>
		</li>
	</xsl:template>
	
	<xsl:template match="HUSB|WIFE|CHIL">
		<li>
			<xsl:choose>
				<xsl:when test="name()='HUSB'">
					<i>Mari: </i>
				</xsl:when>
				<xsl:when test="name()='WIFE'">
					<i>Femme: </i>
				</xsl:when>
				<xsl:when test="name()='CHIL'">
					<i>Enfant: </i>
				</xsl:when>
				<xsl:otherwise>
				</xsl:otherwise>
			</xsl:choose>
			<a href="#{current()/@id}">
				<xsl:value-of select="translate(translate(key('idindiv', current()/@id)/NAME,'/',''),'_',' ')"/>
			</a>				
		</li>
	</xsl:template>	
	
	<xsl:template match="NAME|TITL|SEX|DIV">
		<li>
			<xsl:choose>
				<xsl:when test="name()='NAME'">
					<i>Nom: </i>
				</xsl:when>
				<xsl:when test="name()='TITL'">
					<i>Titre: </i>
				</xsl:when>
				<xsl:when test="name()='SEX'">
					<i>Sexe: </i>
				</xsl:when>
				<xsl:otherwise>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:value-of select="translate(translate(current(),'/',''),'_',' ')"/>
		</li>
	</xsl:template>	

	<xsl:template match="MARR|BIRT|DEAT|BURI|CHR">
		<li>
			<xsl:choose>
				<xsl:when test="name()='MARR'">
					<i>Marriage:</i>
				</xsl:when>
				<xsl:when test="name()='BIRT'">
					<i>Naissance:</i>
				</xsl:when>
				<xsl:when test="name()='DEAT'">
					<i>D&#233;c&#232;s:</i>
				</xsl:when>
				<xsl:when test="name()='BURI'">
					<i>S&#233;pulture:</i>
				</xsl:when>
				<xsl:when test="name()='CHR'">
					<i>Bapt&#234;me:</i>
				</xsl:when>
				<xsl:otherwise>
				</xsl:otherwise>
			</xsl:choose>
			<ul>
				<xsl:apply-templates select="*"/>
			</ul>
		
		</li>
	</xsl:template>
	
	<xsl:template match="DATE|PLAC|DIV">
		<li>
			<xsl:choose>
				<xsl:when test="name()='DATE'">
					Date: 
				</xsl:when>
				<xsl:when test="name()='PLAC'">
					Place: 
				</xsl:when>
				<xsl:when test="name()='DIV'">
					<i>Divorc&#233;(e): </i>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="current()"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:value-of select="translate(translate(current(),'/',''),'_',' ')"/>
		</li>
	</xsl:template>
</xsl:stylesheet>