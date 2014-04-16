Version=$1
Dist=siat-$Version

#todo: check dir
cd ..

mkdir ../$Dist
rm -fr ../$Dist/*

mkdir  ../$Dist/dist
mkdir  ../$Dist/src


cd tools/alone
ant clean

cd ../grs
ant clean

cd ../../
ant clean

cp -r * ../$Dist/src
cp -r etc ../$Dist

ant war
cp build/intra/*war ../$Dist/dist

cd ..
find $Dist -name ".svn" | xargs rm -fr 
tar zcf $Dist-bin.tar.gz --exclude=$Dist/src $Dist 
tar zcf $Dist-src.tar.gz $Dist


