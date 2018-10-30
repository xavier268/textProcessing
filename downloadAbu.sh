#!/bin/bash

URLS=target.list
CORPUS=abuCorpus.utf8

echo "This script will download the raw texts from the site www.abu.cnam.fr"

# "Les url qui nous interressent sont de la forme : http://abu.cnam.fr/cgi-bin/donner_unformated?gilblas1"
# "Il faut donc d'abord créer la liste des fichiers à télécharger ..."

wget -O - http://abu.cnam.fr/BIB/ \
   | grep -oE -e  "(cgi-bin/go).[0-9a-zA-Z]+" \
   | sed -E "s/^.*\?(.*)$/http:\/\/abu.cnam.fr\/cgi-bin\/donner_unformated\?\1/ " > $URLS

echo "Target list was loaded in  $URLS"
echo ""
echo "Now, we download the files and convert to utf8"
echo "" > $CORPUS
cat $URLS | xargs wget -O - | iconv -f LATIN1 -t UTF8 >> $CORPUS









