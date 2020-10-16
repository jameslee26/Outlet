import static org.junit.Assert.*;


public class OutletUnitTest {

	@org.junit.Test
	public void testRate() throws Exception{
		String json = "{\"token\":{\"price\":0.99,\"name\":\"USDC\"},\"rates\":{\"dYdX\":{\"lend\":{\"rate\":6.0,\"monthlyAmount\":\"6.09\"},\"borrow\":{\"rate\":2.8409963399604,\"monthlyAmount\":\"23.44\"},\"term\":\"1 block\",\"name\":\"dYdX\"},\"Compound\":{\"borrow\":{\"rate\":1.8438855679668853,\"monthlyAmount\":\"15.21\"},\"lend\":{\"rate\":5.0,\"monthlyAmount\":\"5.25\"},\"supply\":83975775.143307,\"term\":\"1 block\",\"name\":\"Compound\"}}}";
		double rate = APITest.findRateForUnitTest(json);
		assertEquals(.055, rate, .00001);
	}
	
	/**
	 * note connection is already tested and handled in the code
	 */
	@org.junit.Test
	public void testReturnedJson() {
		String json = APITest.getJson();
		assertNotNull(json);
	}
	
	//thought about test case for rate, but due to the date nature, better test through running the command line prompt.

}
