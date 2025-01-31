В этом задании мы будем работать с иерархическими данными (деревьями), которые хранятся в базе.
По своей природе реляционные базы данных не приспособлены для хранения иерархических структур,
у которых есть связь родитель-наследники. Для организации хранения деревьев в базе данных существует несколько подходов,
у каждого из которых есть свои преимущества и недостатки. Мы будем использовать подход, который называется "Материализованный путь".
Он заключается в хранении полного пути от вершины дерева до узла. В пути (path) храниться цепочка всех предков для каждого узла.
Этот подход удобно использовать, когда нужно получить всех предков текущего узла.

Дерево

```
id	name	path
1	A	null
2	B	1
3	D	1.2
4	E	1.2
5	F	1.2.4
6	G	1.2.4
7	C	1
```

Для примера возьмем курс на Хекслете. У каждого курса есть пререквизит – курс, который нужно пройти, прежде чем приступать к текущему.
Например, прежде чем проходить курс по массивам, нужно пройти курс Java Core. А перед прохождением курса по коллекциям нужно пройти
и курс по массивам и курс Java Core.