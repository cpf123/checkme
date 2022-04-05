function echo_name(){
for file in `ls $1`
do
  if [[ $1"/"$file == *".jar" ]];then
  echo  $1"/"$file
fi

if [ -d $1"/"$file ]
then
##if [[ $1"/"$file == *.jar ]]
echo_name $1"/"$file
#fi
fi
done
}
echo_name /Users/Bloomberg/Downloads/58IM