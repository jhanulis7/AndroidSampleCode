# AndroidSampleCode
Android Sample Code Project WorkSpace

### 1. ComposeViewModelHilt
- Compose Architecture with ViewModel
- ViewModel 을 통해서 State Listener, Event 를 전달받아 State 전달 아키텍쳐 구조
- 모든 화면의 상태는 ViewModel 안에 있습니다. 
- 사용자의 터치 이벤트나 mutableState 의 상태가 변화를 리스너를 통해서 특정 기능을 수행하고, UI 업데이트를 하게 됨
- 모든 화면의 상태는 ViewModel

### 2. HiltRetrofitApp
- HILT 를 통한 Retrofit Inject 방법
- ViewModel 을 통하여 API Response 결과를 UI 업데이트함(XML)
- MutalbeStateFlow 가 아니라 MutableLiveData 사용

### 3. ComposeViewModelHilt
- https://howtodoandroid.com/movielist.json 를 쿼리하여 json 내용을 리스트로 보여줌
- 리스트에는 MovieItem 으로서 name, category, imageUrl 등을 제공함
- ViewModel 과 Retrofit 을 Hilt 를 통해서 주입함
- Flow State 를 통하여 UI 를 갱신하도록 함
- Coroutine 을 통하여 Retrofit 통신함


### 4. CoroutineContentProvider
- Content Provider 를 MVVM 구조로 구현하되, Coroutine 으로 구현
- MVVM : MainActivity > MainViewModel > ContactsRepository > ContactsDataSource
- Compose Permission 구현
- ref: [ref1 link](https://jossypaul.medium.com/loading-data-from-contentprovider-using-coroutines-and-livedata-34aa5e79b8ba) , 
       [ref2 link](https://velog.io/@cchloe2311/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-ContentProvider)
       [permission link](https://google.github.io/accompanist/permissions/)

### 5. Kakao Login
- 카카오 로그인을 Compose 로 구현해보기
- Kakao LogIn/Out/State + Coroutine + Flow + ViewModel + Compose
- Kakao LogIn Sdk : https://developers.kakao.com/docs/latest/ko/kakaologin/android#logout
- Kakao App key 받기 및 소스에서 키 숨기기
- ViewModel 에서 mutableStateFlow 로 로그인 상태를 Observe 받은 뒤, Compose 에서 collectAsState 를 통해서 State(MutableState)로 상태관리
- Hilt 는 구지 안쓰고 컨텍스트를 받기 위해서 , viewModel to AndroidViewModel 사용
- ref: [정대리 유튜브](https://www.youtube.com/watch?v=LV0pcMuBUKI&list=PLgOlaPUIbynpFHXeEORmvIOoiNVgSsWeq&index=29)
