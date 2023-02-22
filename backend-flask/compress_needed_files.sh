# change directory to desired path
cd /Users/varian/sample-env/backend-flask/

# delete existing zip file
echo "deleting existing zip file"
rm flaskapp.zip

# archive desired files. Unzip will not make directory. (in-place unzipping)
echo "zipping files to flaskapp.zip!!"
zip flaskapp.zip -r ./app.py ./conf requirements.txt


