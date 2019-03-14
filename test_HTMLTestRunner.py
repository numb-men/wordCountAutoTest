import unittest
from HTMLTestRunner_Chart import HTMLTestRunner


class case_01(unittest.TestCase):

    @classmethod
    def setUpClass(cls):
        pass

    @classmethod
    def tearDownClass(cls):
        pass

    def test_case1(self):
        print("测试1")
        self.assertTrue(False)

    def test_case2(self):
        raise TypeError
        self.assertTrue(False)

    def test_case3(self):
        raise TypeError

    def test_case4(self):
        self.assertTrue(True)
        raise TypeError

    def test_case5(self):
        raise TypeError
        self.assertTrue(False)


if __name__ == '__main__':
    suite = unittest.TestLoader().loadTestsFromTestCase(case_01)
    runner = HTMLTestRunner(
        title="测试报告",
        description="",
        stream=open("./test_result.html", "wb"),
        verbosity=2,
        retry=0,
        save_last_try=True)
    runner.run(suite)
