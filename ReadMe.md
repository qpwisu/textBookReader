# textBookReader

[코틀린] txt파일을 읽어드리는 첫번째 토이프로젝트앱 

## **프로젝트 계획 이유**

그동안 공부했던 외부 저장소, 내부저장소, layout, 코틀린 기초를 다양하게 사용할 수 있는 앱을 만들어 보고자합니다

## **설명**

<img src= https://user-images.githubusercontent.com/28581494/152361710-aa616e16-6ee0-44dd-ab00-d255c95314a8.png  width="400" height="700">
<img src= https://user-images.githubusercontent.com/28581494/152363646-4eb98926-aca2-4fc5-8dc9-e94dfe9b10e3.png  width="400" height="700">

왼쪽 홈 화면- 리사이클러뷰로 파일을 찾아 저장을하면 이 곳에 제목이 저장됩니다. 파일 내용과 이름은 따로 `SharedPreferences` 에 저장했습니다 

오른 쪽 화면- 외부저장소에 접근하는 방법을 찾다 안드로이드 10부터는 기존의 방법을 사용하면 안되고 

**MediaStore**나 **SAF(Storage Access Framework)** 를 이용해야해서 SAF를 사용하여 접근 하였습니다.

<img src= https://user-images.githubusercontent.com/28581494/152363823-be67dd2b-daac-46c0-9ffc-9d20d6e3b134.png  width="400" height="700">

text들은 화면에 들어갈 수 있는 글자들을 나누어 배열에 저장하고 이를 canvas를 통해 onDraw에서 화면에 뿌려줬습니다. 그리고 화면 오른쪽 왼쪽에 투명한 view를 두어 클리하면 다음장으로 넘어가게 하였습니다. 

## **배운점 + 어려웠던점**

1. canvas, ondraw, paint 를 이용하여 텍스트를 화면에 뿌려주는게 상당히 어려웠습니다. 그리고 글이 보이는 화면에서onDraw되기전에 화면의 width,height을 알아내 글자들을 배열로 나눠줬어야되는데 이 방법을 몰라 그 전 화면의 크기를 이용하였습니다.
2. text를 여러줄 출력하기 위해 `StaticLayout.Builder.obtain` 를 이용하였는데 줄바꿈문자, 글자사이  빈칸, 글자크기 등 신경쓸게 많아 한 화면에 들어갈 문자를 배열로 나누기 힘들었습니다. 
3. SAF를 통해 외부 저장소에 접근하여 파일을 읽고 저장하는 방법을 배웠습니다.
4. view 생명주기의 중요성을 느낄 수 있었고 코드가 조금 길어지니 객체 지향 설계를 더 공부해야겠다 생각했습니다.
