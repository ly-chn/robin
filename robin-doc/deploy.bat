set -e

npm run docs:build

cd .vuepress/dist

# echo 'www.example.com' > CNAME

git init
git add -A
git commit -m 'deploy'

git push -f https://github.com/ly-chn/docs-robin.git master:gh-pages

cd -